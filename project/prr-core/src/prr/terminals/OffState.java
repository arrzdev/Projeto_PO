package prr.terminals;

import java.io.Serializable;

public class OffState extends TerminalState implements Serializable {
  public OffState(Terminal t) {
    super(t);
  }

  // Block all communications
  @Override
  public boolean allowReceiveText() {
    return false;
  }

  @Override
  public boolean allowReceiveVideo() {
    return false;
  }

  @Override
  public boolean allowSendText() {
    return false;
  }

  @Override
  public boolean allowSendVideo() {
    return false;
  }

  @Override
  public String toString() {
    return "OFF";
  }
}
