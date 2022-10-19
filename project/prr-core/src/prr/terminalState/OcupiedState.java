package prr.terminalState;

public class OcupiedState implements terminalState {
    private String state = "Ocupied";
    private String changeState = "Busy";

    public String getState() {
        return state;
    }

    public String changeState() {
        return changeState;
    }
}
