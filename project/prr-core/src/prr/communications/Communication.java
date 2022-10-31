package prr.communications;

import java.io.Serializable;

import prr.terminals.Terminal;

public abstract class Communication implements Serializable {
    // TODO: communication id not implemented

    private Terminal _from;
    private Terminal _to;
    private CommunicationState _state;

    public Communication(Terminal from, Terminal to) {
        _from = from;
        _to = to;
        _state = new CommunicationState();
    }

    public Terminal getFrom() {
        return _from;
    }

    public Terminal getTo() {
        return _to;
    }

    public bool isOnGoing() {
        return _state.isOnGoing();
    }

    public bool isFinished() {
        return _state.isFinished();
    }

    public void end() {
        _state.end();
    }

    abstract public String type();

    @Override
    public String toString() {
        return String.format("%s|idCommunication|%s|%s|units|price|%s", type(), _from.getId(), _to.getId(),
                _state);
    }
}