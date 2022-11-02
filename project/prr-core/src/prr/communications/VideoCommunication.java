package prr.communications;

import java.io.Serializable;

import prr.terminals.Terminal;

public class VideoCommunication extends Communication implements Serializable {
  public VideoCommunication(Terminal from, Terminal to) {
    super(from, to);
  }

  public String getType() {
    return "VIDEO";
  }

}
