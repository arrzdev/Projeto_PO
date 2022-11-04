package prr.communications;

import java.io.Serializable;

import prr.payments.PaymentPlan;
import prr.terminals.Terminal;

public class TextCommunication extends Communication implements Serializable {
  private String _message;

  public TextCommunication(int id, Terminal from, Terminal to, String message) {
    super(id, from, to);
    _message = message;
    setCommunicationState(new FinishedCommunicationState(this));
  }

  public String getMessage() {
    return _message;
  }

  public int getUnits() {
    return _message.length();
  }

  public String getType() {
    return "TEXT";
  }
}
