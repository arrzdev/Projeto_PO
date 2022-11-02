package prr.communications;

import java.io.Serializable;

public class OngoingCommunicationState extends CommunicationState implements Serializable {
    public OngoingCommunicationState(Communication com) {
        super(com);
    }

    public bool isOnGoing() {
        return true;
    }

    public bool isFinished() {
        return false;
    }

    @Override
    public String toString() {
        return "ONGOING";
    }
}
