package prr.communications;

import java.io.Serializable;

public class FinishedCommunicationState extends CommunicationState implements Serializable {
    public FinishedCommunicationState(Communication com) {
        super(com);
    }

    public bool isOnGoing() {
        return false;
    }

    public bool isFinished() {
        return true;
    }

    @Override
    public String toString() {
        return "FINISHED";
    }
}
