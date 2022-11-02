package prr.communications;

import java.io.Serializable;

import prr.terminals.Terminal;

public class TextCommunication extends Communication implements Serializable {
    private String _message;

    public TextCommunication(Terminal from, Terminal to, String message) {
        super(from, to);
        _message = message;
    }

    public String getMessage() {
        return _message;
    }

    public String getType() {
        return "TEXT";
    }

}
