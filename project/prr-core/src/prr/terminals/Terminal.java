package prr.terminals;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

import prr.communications.Communication;
import prr.communications.FinishedCommunicationState;
import prr.communications.TextCommunication;
import prr.communications.VoiceCommunication;

import prr.exceptions.UnsupportedAtOriginException;
import prr.notifications.ClientNotification;
import prr.payments.PaymentPlan;
import prr.clients.Client;

public class Terminal implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202208091753L;

  private Client _client;
  private TerminalState _state = new IdleState(this);
  private TerminalState _oldState;

  private String _id;

  private double _payments = 0;
  private double _debts = 0;

  private TreeMap<String, Terminal> _friends = new TreeMap<String, Terminal>();

  private ArrayList<Communication> _sentComms = new ArrayList<Communication>();
  private ArrayList<Communication> _receivedComms = new ArrayList<Communication>();
  private Communication _currentCommunication;

  private ArrayList<Client> _clients_to_notify = new ArrayList<Client>();

  public Terminal(String id, Client client) {
    _id = id;
    _client = client;
  }

  public void addClientToNotify(Client client) {
    if (!_clients_to_notify.contains(client)) {
      _clients_to_notify.add(client);
    }
  }

  public void notifyClients(ClientNotification notification) {
    for (Client client : _clients_to_notify) {
      if (client.canReceiveNotifications()) { // multiple bug here
        client.addNotification(notification);
      }
    }

    _clients_to_notify.clear();
  }

  public void videoCommunicationIsPossible() throws UnsupportedAtOriginException {
    throw new UnsupportedAtOriginException();
  }

  public void setTerminalState(TerminalState state) {
    _state = state;
  }

  public TerminalState getState() {
    return _state;
  }

  public void setOldState(TerminalState state) {
    _oldState = state;
  }

  public TerminalState getOldState() {
    return _oldState;
  }

  public Communication getCurrentCommunication() {
    return _currentCommunication;
  }

  public void setCurrentCommunication(Communication communication) {
    _currentCommunication = communication;
  }

  public Client getClient() {
    return _client;
  }

  public String getId() {
    return _id;
  }

  public double getPayments() {
    return _payments;
  }

  public double getDebts() {
    return _debts;
  }

  public double pay(Communication communication) {
    double cost = communication.getCost();
    _debts -= cost;
    _payments += cost;
    communication.setPaid();

    _client.getPaymentPlan().update();

    return cost;
  }

  public double addDebt(Communication communication) {
    PaymentPlan paymentPlan = _client.getPaymentPlan();

    double cost = paymentPlan.cost(communication);
    _debts += cost;

    if (_client.getPaymentPlan().toString().equals("GOLD")) {
      if (communication.getType() == "VIDEO")
        paymentPlan.increaseStreak();

      else
        paymentPlan.resetStreak();
    }

    else if (_client.getPaymentPlan().toString().equals("PLATINIUM")) {
      if (communication.getType() == "TEXT")
        paymentPlan.increaseStreak();

      else
        paymentPlan.resetStreak();
    }

    paymentPlan.update();

    return cost;
  }

  public void registerSentCommunication(Communication comm) {
    _sentComms.add(comm);
  }

  public ArrayList<Communication> getSentComms() {
    return _sentComms;
  }

  public void registerReceivedCommunication(Communication comm) {
    _receivedComms.add(comm);
  }

  public ArrayList<Communication> getReceivedComms() {
    return _receivedComms;
  }

  public int getTotalCommunicationsCount() {
    return _sentComms.size() + _receivedComms.size();
  }

  public void addFriend(Terminal friend) {
    _friends.put(friend.getId(), friend);
  }

  public void removeFriend(Terminal friend) {
    _friends.remove(friend.getId());
  }

  public boolean isFriend(String friendId) {
    return _friends.get(friendId) != null;
  }

  /**
   * Checks if this terminal can end the current interactive communication.
   *
   * @return true if this terminal is busy (i.e., it has an active interactive
   *         communication) and
   *         it was the originator of this communication.
   **/
  public boolean canEndCurrentCommunication() {
    return _state.canEndCurrentCommunication() && _currentCommunication.getOriginTerminal().getId().equals(_id);
  }

  /**
   * Checks if this terminal can start a new communication.
   *
   * @return true if this terminal is neither off neither busy, false otherwise.
   **/
  public boolean canStartCommunication() {
    return !(_state.toString().equals("OFF") || _state.toString().equals("BUSY"));
  }

  public TextCommunication sendTextCommunication(int communication_id, Terminal terminal, String textMessage) {
    TextCommunication textCom = new TextCommunication(communication_id, this, terminal, textMessage);

    registerSentCommunication(textCom);

    terminal.registerReceivedCommunication(textCom);

    double cost = addDebt(textCom);

    textCom.setCost(cost);

    return textCom;
  }

  public VoiceCommunication startVoiceCommunication(int communication_id, Terminal terminal) {
    VoiceCommunication voiceCom = new VoiceCommunication(communication_id, this, terminal);

    _currentCommunication = voiceCom;

    setOldState(getState());

    setTerminalState(new BusyState(this));

    registerSentCommunication(voiceCom);

    terminal.setCurrentCommunication(_currentCommunication);

    terminal.setOldState(terminal.getState());

    terminal.getState().changeToBusy();

    terminal.registerReceivedCommunication(voiceCom);

    return voiceCom;
  }

  public double endCurrentVoiceCommunication(int duration) {
    VoiceCommunication currentVoiceComm = (VoiceCommunication) _currentCommunication;

    currentVoiceComm.setDuration(duration);
    currentVoiceComm.setCommunicationState(new FinishedCommunicationState(currentVoiceComm));

    Terminal destination_terminal = currentVoiceComm.getDestination();
    destination_terminal.setCurrentCommunication(null);
    destination_terminal.setTerminalState(destination_terminal.getOldState());
    destination_terminal.setOldState(null);

    setCurrentCommunication(null);
    setTerminalState(getOldState());
    setOldState(null);

    double cost = addDebt(currentVoiceComm);

    currentVoiceComm.setCost(cost);

    return cost;
  }

  public String getType() {
    return "BASIC";
  }

  @Override
  public String toString() {

    String friends = "";

    if (_friends.size() != 0)
      friends = "|" + String.join(", ", _friends.keySet());

    return String.format("%s|%s|%s|%s|%d|%d%s", getType(), getId(), getClient().getId(),
        _state.toString(), Math.round(_payments), Math.round(_debts),
        friends);
  }
}
