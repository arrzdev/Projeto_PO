package prr.terminals;

public class OffState implements terminalState {
  private String state = "TurnedOff";
  private String changeState = "Busy";

  public String getState() {
    return state;
  }

  public String changeState() {
    return changeState;
  }
}
