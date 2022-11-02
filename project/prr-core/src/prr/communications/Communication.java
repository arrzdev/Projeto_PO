package prr.communications;

import java.io.Serializable;

import prr.terminals.Terminal;

public abstract class Communication implements Serializable {
    // TODO: communication id not implemented

    private Terminal _sender;
    private Terminal _receiver;
    private CommunicationState _state;

    public Communication(Terminal sender, Terminal receiver) {
        _sender = sender;
        _receiver = receiver;
        _state = new OngoingCommunicationState(this);
    }

    public void setCommunicationState(CommunicationState state) {
        _state = state;
    }

    public Terminal getSender() {
        return _sender;
    }

    public Terminal getReceiver() {
        return _receiver;
    }

    public bool isOnGoing() {
        return _state.isOnGoing();
    }

    public bool isFinished() {
        return _state.isFinished();
    }

    public void end() {
        setCommunicationState(new FinishedCommunicationState(this));
        _receiver.endCurrentCommunication();
    }

    abstract public String getType();

    @Override
    public String receiverString() {
        return String.format("%s|idCommunication|%s|%s|units|price|%s", getType(), _sender.getId(), _receiver.getId(),
                _state);
    }
}