package prr.terminals;

import prr.exceptions.TerminalAlreadyOnException;
import prr.exceptions.TerminalAlreadyOffException;
import prr.exceptions.TerminalAlreadySilenceException;
import prr.exceptions.DestinationIsOffException;
import prr.exceptions.DestinationIsBusyException;
import prr.exceptions.DestinationIsSilentException;

import java.io.Serializable;

public abstract class TerminalState implements Serializable {
  protected Terminal _terminal;

  public TerminalState(Terminal t) {
    _terminal = t;
  }

  public void turnOn() throws TerminalAlreadyOnException {
    _terminal.setTerminalState(new IdleState(_terminal));
  }

  public void turnOff() throws TerminalAlreadyOffException {
    _terminal.setTerminalState(new OffState(_terminal));
  }

  public void changeToSilence() throws TerminalAlreadySilenceException {
    _terminal.setTerminalState(new SilenceState(_terminal));
  }

  public void changeToBusy() {
    _terminal.setTerminalState(new BusyState(_terminal));
  }

  // manages whether there is something that blocks the communication or not and
  // throw the exceptions
  public void voiceCommunicationIsPossible()
      throws DestinationIsOffException, DestinationIsBusyException, DestinationIsSilentException {
  }

  public void videoCommunicationIsPossible()
      throws DestinationIsOffException, DestinationIsBusyException, DestinationIsSilentException {
  }

  public void textCommunicationIsPossible()
      throws DestinationIsOffException {
  }

  public boolean canEndCurrentCommunication() {
    return false;
  }

  public boolean allowReceiveText() {
    return true;
  }

  public boolean allowSendText() {
    return true;
  }

  public boolean allowReceiveVideo() {
    return _terminal.getType() == "FANCY";
  }

  public boolean allowSendVideo() {
    return _terminal.getType() == "FANCY";
  }
}
