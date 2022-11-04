package prr.communications;

import java.io.Serializable;

import prr.terminals.Terminal;
import prr.payments.PaymentPlan;

public class VoiceCommunication extends Communication implements Serializable {
  private int _duration;

  public VoiceCommunication(int id, Terminal from, Terminal to) {
    super(id, from, to);
  }

  public String getType() {
    return "VOICE";
  }

  public void setDuration(int duration) {
    _duration = duration;
  }

  public int getUnits() {
    return _duration;
  }

}
