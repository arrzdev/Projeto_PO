package prr.terminals;

public class BusyState implements TerminalState {
  private String stateName = "BUSY";
  private String changeState = "Busy";

  public String getState() {
    return state;
  }

  public String changeState() {
    return changeState;
  }
}
