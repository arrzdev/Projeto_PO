package prr.terminals;

public class IdleState implements TerminalState {
  private String state = "Wait";
  private String changeState = "Busy";

  public String getState() {
    return state;
  }

  public String changeState() {
    return changeState;
  }
}
