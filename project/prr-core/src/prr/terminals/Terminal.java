package prr.terminals;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

import prr.notifications.CustomNotification;
import prr.notifications.AppNotification;

import prr.communications.Communication;
import prr.communications.FinishedCommunicationState;
import prr.communications.TextCommunication;
import prr.communications.VoiceCommunication;

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
  private Communication _currentComm;

  private ArrayList<CustomNotification> _pendingNotifications = new ArrayList<CustomNotification>();

  // TODO: Receive terminal state (object or string)
  public Terminal(String id, Client client) {
    _id = id;
    _client = client;
  }

  public void pushNotification(CustomNotification notification) {
    _pendingNotifications.add(notification);
  }

  public void resetNotification() {
    _pendingNotifications = new ArrayList<CustomNotification>();
  }

  public ArrayList<CustomNotification> getNotifications() {
    return _pendingNotifications;
  }

  public void setTerminalState(TerminalState state) {
    _state = state;
  }

  public TerminalState getTerminalState() {
    return _state;
  }

  public void setOldTerminalState(TerminalState state) {
    _oldState = state;
  }

  public TerminalState getOldTerminalState() {
    return _oldState;
  }

  public Communication getCurrentCommunication() {
    return _currentComm;
  }

  public void setCurrentCommunication(Communication communication) {
    _currentComm = communication;
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

  public void registerSentCoommunication(Communication comm) {
    _sentComms.add(comm);
  }

  public ArrayList<Communication> getSentComms() {
    return _sentComms;
  }

  public void registerReceivedCoommunication(Communication comm) {
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

  public void removeFriend(String friendId) {
    _friends.remove(friendId);
  }

  public void removeFriend(Terminal friend) {
    _friends.remove(friend.getId());
  }

  public Terminal getFriend(String friendId) {
    return _friends.get(friendId);
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
    // return false;
    // TODO: change .toString() blablabla
    return _state.toString().equals("BUSY") && _currentComm.getSender().getId().equals(_id);
  }

  /**
   * Checks if this terminal can start a new communication.
   *
   * @return true if this terminal is neither off neither busy, false otherwise.
   **/
  public boolean canStartCommunication() {
    return !(_state.toString().equals("OFF") || _state.toString().equals("BUSY"));
  }

  public TextCommunication sendTextCommunication(int communication_id, Terminal receiver, String textMessage) {
    TextCommunication textCom = new TextCommunication(communication_id, this, receiver, textMessage);

    registerSentCoommunication(textCom);

    receiver.registerReceivedCoommunication(textCom);

    double cost = addDebt(textCom);

    textCom.setCost(cost);

    return textCom;
  }

  public VoiceCommunication startVoiceCommunication(int communication_id, Terminal receiver) {
    VoiceCommunication voiceCom = new VoiceCommunication(communication_id, this, receiver);

    _currentComm = voiceCom;

    setOldTerminalState(getTerminalState());

    setTerminalState(new BusyState(this));

    registerSentCoommunication(voiceCom);

    receiver.setCurrentCommunication(_currentComm);

    receiver.setOldTerminalState(receiver.getTerminalState());

    receiver.setTerminalState(new BusyState(receiver));

    receiver.registerReceivedCoommunication(voiceCom);

    return voiceCom;
  }

  public double endCurrentVoiceCommunication(int duration) {
    VoiceCommunication currentVoiceComm = (VoiceCommunication) _currentComm;

    currentVoiceComm.setDuration(duration);
    currentVoiceComm.setCommunicationState(new FinishedCommunicationState(currentVoiceComm));

    Terminal receiver = currentVoiceComm.getReceiver();
    receiver.setCurrentCommunication(null);
    receiver.setTerminalState(receiver.getOldTerminalState());
    receiver.setOldTerminalState(null);

    setCurrentCommunication(null);
    setTerminalState(getOldTerminalState());
    setOldTerminalState(null);

    double cost = addDebt(currentVoiceComm);

    currentVoiceComm.setCost(cost);

    return cost;
  }

  public String getTerminalType() {
    return "BASIC";
  }

  @Override
  public String toString() {

    String friends = "";

    if (_friends.size() != 0)
      friends = "|" + String.join(", ", _friends.keySet());

    return String.format("%s|%s|%s|%s|%d|%d%s", getTerminalType(), getId(), getClient().getId(),
        _state.toString(), Math.round(_payments), Math.round(_debts),
        friends);
  }
}
