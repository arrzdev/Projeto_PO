package prr.terminals;

import javax.swing.plaf.metal.MetalBorders.TextFieldBorder;

import java.io.Serializable;

public class BusyState extends TerminalState implements Serializable {
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

  @Override
  public String toString() {
    return "BUSY";
  }
}
