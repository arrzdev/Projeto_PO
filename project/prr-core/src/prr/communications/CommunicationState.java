package prr.communications;

import java.io.Serializable;

public abstract class CommunicationState implements Serializable {
    protected Communication _communication;

    public CommunicationState(Communication com) {
        _communication = com;
    }

    public void end() {
        _communication.setCommunicationState(new FinishedCommunicationState(_communication));
    }

    public abstract boolean isOnGoing();

    public abstract boolean isFinished();
}
