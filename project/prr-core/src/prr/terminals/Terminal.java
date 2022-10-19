package prr.terminals;

import java.io.Serializable;

import prr.clients.Client;
import prr.database.AbstractModel;
import prr.database.ClientCollection;
import prr.terminalState.terminalState;

abstract public class Terminal extends AbstractModel implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202208091753L;

  private String id;
  private Client client;
  private terminalState state;

  // TODO: Receive terminal state (object or string)
  public Terminal(String _id, Client _client, TerminalState _state) {
    this.id = _id;
    this.client = _client;
    this.state = _state;
  }

  public Client getClient() {
    return this.client;
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
