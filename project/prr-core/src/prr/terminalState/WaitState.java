package prr.terminalState;

public class WaitState implements terminalState {
    private String state = "Wait";
    private String changeState = "Busy";

    public String getState() {
        return state;
    }

    public String changeState() {
        return changeState;
    }
}
