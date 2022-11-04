package prr.terminals;

import prr.exceptions.TerminalAlreadySilenceException;
import prr.exceptions.DestinationIsSilentException;
import prr.notifications.SilentToIdle;
import java.io.Serializable;

public class SilenceState extends TerminalState implements Serializable {
  public SilenceState(Terminal t) {
    super(t);
  }

  public void turnOnTerminal() {
    _terminal.notifyClients(new SilentToIdle(_terminal.getId()));
    _terminal.setTerminalState(new IdleState(_terminal));
  }

  public void changeToSilence() throws TerminalAlreadySilenceException {
    throw new TerminalAlreadySilenceException();
  }

  public void voiceCommunicationIsPossible() throws DestinationIsSilentException {
    throw new DestinationIsSilentException();
  }

  public void videoCommunicationIsPossible() throws DestinationIsSilentException {
    throw new DestinationIsSilentException();
  }

  @Override
  public String toString() {
    return "SILENCE";
  }
}
