package prr.terminals;

public class SilenceState extends TerminalState {
  public SilenceState(Terminal t) {
    super(t);
  }

  // Block receiving of video communications
  @Override
  public boolean allowReceiveVideo() {
    return false;
  }
}
