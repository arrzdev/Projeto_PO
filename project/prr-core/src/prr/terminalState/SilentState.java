package prr.terminalState;

public class SilentState implements terminalState {
    private String state = "Silent";
    private String changeState = "Busy";

    public String getState() {
        return state;
    }

    public String changeState() {
        return changeState;
    }
}
