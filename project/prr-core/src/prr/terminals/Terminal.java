package prr.terminals;

import java.io.Serializable;
import java.util.ArrayList;

import prr.communications.Communication;
import prr.clients.Client;
import prr.database.AbstractModel;
import prr.database.ClientCollection;

public class Terminal extends AbstractModel implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202208091753L;

  private Client _client;

  protected TerminalState _state = new IdleState(this);
  private int _payments = 0;
  private int _debts = 0;

  private ArrayList<String> _friends = new ArrayList<String>();

  private ArrayList<Communication> _sentComms = new ArrayList<Communication>();
  private ArrayList<Communication> _receivedComms = new ArrayList<Communication>();

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

  public void addFriend(String friendId) {
    _friends.add(friendId);
  }

  public void removeFriend(String friendId) {
    _friends.remove(friendId);
  }

  public int totalCommunicationsCount() {
    return _sentComms.size() + _receivedComms.size();
  }

  /**
   * Checks if this terminal can end the current interactive communication.
   *
   * @return true if this terminal is busy (i.e., it has an active interactive
   *         communication) and
   *         it was the originator of this communication.
   **/
  public boolean canEndCurrentCommunication() {
    return false;
  }

  /**
   * Checks if this terminal can start a new communication.
   *
   * @return true if this terminal is neither off neither busy, false otherwise.
   **/
  public boolean canStartCommunication() {
    return true;
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
