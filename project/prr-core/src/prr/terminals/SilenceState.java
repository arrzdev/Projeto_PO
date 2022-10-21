package prr.terminals;

import java.io.Serializable;

public class SilenceState extends TerminalState implements Serializable {
  public SilenceState(Terminal t) {
    super(t);
  }

  // Block receiving of video communications
  @Override
  public boolean allowReceiveVideo() {
    return false;
  }

  @Override
  public String toString() {
    return "SILENCE";
  }
}
