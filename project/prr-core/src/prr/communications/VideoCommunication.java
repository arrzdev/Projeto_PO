package prr.communications;

import java.io.Serializable;

import prr.terminals.Terminal;
import prr.payments.PaymentPlan;

public class VideoCommunication extends Communication implements Serializable {
  public int _duration;

  public VideoCommunication(int id, Terminal from, Terminal to) {
    super(id, from, to);
  }

  public String getType() {
    return "VIDEO";
  }

  public void setDuration(int duration) {
    _duration = duration;
  }

  public int getUnits() {
    return _duration;
  }
}
