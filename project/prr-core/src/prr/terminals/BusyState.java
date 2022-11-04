package prr.terminals;

import prr.exceptions.DestinationIsBusyException;
import prr.notifications.BusyToIdle;
import java.io.Serializable;

public class BusyState extends TerminalState implements Serializable {
  public BusyState(Terminal t) {
    super(t);
  }

  public void turnOnTerminal() {
    _terminal.notifyClients(new BusyToIdle(_terminal.getId()));
    _terminal.setTerminalState(new IdleState(_terminal));
  }

  public void voiceCommunicationIsPossible() throws DestinationIsBusyException {
    throw new DestinationIsBusyException();
  }

  public void videoCommunicationIsPossible() throws DestinationIsBusyException {
    throw new DestinationIsBusyException();
  }

  public boolean canEndCurrentCommunication() {
    return true;
  }

  @Override
  public String toString() {
    return "BUSY";
  }
}
