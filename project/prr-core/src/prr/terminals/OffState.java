package prr.terminals;

import java.io.Serializable;

import prr.exceptions.TerminalAlreadyOffException;
import prr.notifications.OffToSilent;
import prr.notifications.OffToIdle;
import prr.exceptions.DestinationIsOffException;

public class OffState extends TerminalState implements Serializable {
  public OffState(Terminal t) {
    super(t);
  }

  public void turnOffTerminal() throws TerminalAlreadyOffException {
    throw new TerminalAlreadyOffException();
  }

  public void turnOnTerminal() {
    _terminal.notifyClients(new OffToIdle(_terminal.getId()));
    _terminal.setTerminalState(new IdleState(_terminal));
  }

  public void changeToSilence() {
    _terminal.notifyClients(new OffToSilent(_terminal.getId()));
    _terminal.setTerminalState(new SilenceState(_terminal));
  }

  public void voiceCommunicationIsPossible() throws DestinationIsOffException {
    throw new DestinationIsOffException();
  }

  public void videoCommunicationIsPossible() throws DestinationIsOffException {
    throw new DestinationIsOffException();
  }

  public void textCommunicationIsPossible() throws DestinationIsOffException {
    throw new DestinationIsOffException();
  }

  @Override
  public String toString() {
    return "OFF";
  }
}
