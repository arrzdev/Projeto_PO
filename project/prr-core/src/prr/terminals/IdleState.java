package prr.terminals;

import java.io.Serializable;
import prr.exceptions.TerminalAlreadyOnException;

public class IdleState extends TerminalState implements Serializable {
  public IdleState(Terminal t) {
    super(t);
  }

  public void turnOn() throws TerminalAlreadyOnException {
    throw new TerminalAlreadyOnException();
  }

  @Override
  public String toString() {
    return "IDLE";
  }
}
