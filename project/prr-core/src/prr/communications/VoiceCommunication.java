package prr.communications;

import java.io.Serializable;

public class VoiceCommunication extends Communication implements Serializable {
    private String _message;

    public VoiceCommunication(Terminal from, Terminal to, String message) {
        super(from, to);
    }

    public String getMessage() {
        return _message;
    }

    public String type() {
        return "VOICE";
    }

}
