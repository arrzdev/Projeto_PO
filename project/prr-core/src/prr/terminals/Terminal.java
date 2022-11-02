package prr.terminals;

import java.io.Serializable;
import java.util.ArrayList;

import prr.communications.Communication;
import prr.clients.Client;

public class Terminal implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202208091753L;

  private Client _client;
  private TerminalState _state = new IdleState(this);

  private String _id;

  // TODO: check if it is int or long
  private int _payments = 0;
  private int _debts = 0;

  private ArrayList<String> _friends = new ArrayList<String>();

  private ArrayList<Communication> _sentComms = new ArrayList<Communication>();
  private ArrayList<Communication> _receivedComms = new ArrayList<Communication>();
  private Communication _currentComm;

  // TODO: Receive terminal state (object or string)
  public Terminal(String id, Client client) {
    _id = id;
    _client = client;
  }

  public void setTerminalState(TerminalState state) {
    _state = state;
  }

  public Client getClient() {
    return _client;
  }

  public String getId() {
    return _id;
  }

  public long getPayments() {
    return _payments;
  }

  public long getDebts() {
    return _debts;
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

  public void addFriend(String friendId) {
    if (!_friends.contains(friendId))
      _friends.add(friendId);
  }

  public void removeFriend(String friendId) {
    _friends.remove(friendId);
  }

  public boolean isFriend(String friendId) {
    return _friends.contains(friendId);
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
    return _currentComm != null && _state.equals("BUSY") && _currentComm.getSender().getId().equals(_id);
  }

  /**
   * Checks if this terminal can start a new communication.
   *
   * @return true if this terminal is neither off neither busy, false otherwise.
   **/
  public boolean canStartCommunication() {
    return !(_state.equals("OFF") || _state.equals("BUSY"));
  }

  public void endCurrentCommunication() {

  }

  public String getTerminalType() {
    return "BASIC";
  }

  @Override
  public String toString() {
    String friends = "";

    if (_friends.size() != 0) {
      friends = "|" + String.join(", ", _friends);
    }

    return String.format("%s|%s|%s|%s|0|0%s", getTerminalType(), getId(), getClient().getId(), _state.toString(),
        friends);
  }
}
