package prr.terminals;

public class OffState extends TerminalState {
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
}
