package prr.communications;

import java.io.Serializable;

import prr.terminals.Terminal;

public abstract class Communication implements Serializable {
  // TODO: communication id not implemented
  private int _id;

  private Terminal _sender;
  private Terminal _receiver;
  private CommunicationState _state;

  public Communication(Terminal sender, Terminal receiver) {
    _sender = sender;
    _receiver = receiver;
    _state = new OngoingCommunicationState(this);
  }

  public void setCommunicationState(CommunicationState state) {
    _state = state;
  }

  public Terminal getSender() {
    return _sender;
  }

  public Terminal getReceiver() {
    return _receiver;
  }

  public boolean isOnGoing() {
    return _state.isOnGoing();
  }

  public boolean isFinished() {
    return _state.isFinished();
  }

  public void end() {

  }

  public int getId() {
    return _id;
  }

  abstract public String getType();

  public String toString() {
    return String.format("%s|idCommunication|%s|%s|units|price|%s", getType(), _sender.getId(), _receiver.getId(),
        _state);
  }
}