package prr.terminals;

import javax.swing.plaf.metal.MetalBorders.TextFieldBorder;

public class BusyState extends TerminalState {
  public BusyState(Terminal t) {
    super(t);
  }

  // Block sending of communications
  @Override
  public boolean allowSendText() {
    return false;
  }

  @Override
  public boolean allowSendVideo() {
    return false;
  }

  // Block receiving of video communications
  @Override
  public boolean allowReceiveVideo() {
    return false;
  }
}
