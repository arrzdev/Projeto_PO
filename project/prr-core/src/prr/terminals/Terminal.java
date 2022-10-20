package prr.terminals;

import java.io.Serializable;
import java.util.ArrayList;

import prr.clients.Client;
import prr.database.AbstractModel;
import prr.database.ClientCollection;

public class Terminal extends AbstractModel implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202208091753L;

  private Client _client;

  protected TerminalState _state = new IdleState(this);

  private ArrayList<String> _friends = new ArrayList<String>();

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
    return false;
  }
}
