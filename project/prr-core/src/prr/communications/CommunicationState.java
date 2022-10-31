package prr.communications;

import java.io.Serializable;

public class CommunicationState implements Serializable {
    private String _state; // "ONGOING" or "FINISHED"

    public CommunicationState() {
        _state = "ONGOING";
    }

    public void end() {
        _state = "FINISHED";
    }

    public boolean isOnGoing() {
        return _state.equals("ONGOING");
    }

    public boolean isFinished() {
        return isOnGoing() ? false : true;
    }

    @Override
    public String toString() {
        return _state;
    }
}
