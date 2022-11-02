package prr.communications;

import java.io.Serializable;

import prr.terminals.Terminal;

public class VoiceCommunication extends Communication implements Serializable {
  public VoiceCommunication(Terminal from, Terminal to) {
    super(from, to);
  }

  public String getType() {
    return "VOICE";
  }

}
