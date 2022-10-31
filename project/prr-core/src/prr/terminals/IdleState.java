package prr.terminals;

import java.io.Serializable;

public class IdleState extends TerminalState implements Serializable {
  public IdleState(Terminal t) {
    super(t);
  }

  @Override
  public String toString() {
    return "IDLE";
  }
}
