package prr.payments;

import java.io.Serializable;
import prr.clients.Client;

import prr.communications.TextCommunication;
import prr.communications.VideoCommunication;
import prr.communications.VoiceCommunication;

public class NormalPaymentPlan extends PaymentPlan implements Serializable {
  public NormalPaymentPlan(Client c) {
    super(c);
  }

  public void update() {
    if (_client.getBalance() > 500)
      _client.setPaymentPlan(new GoldPaymentPlan(_client));
  }

  public double cost(TextCommunication com) {
    double chars = com.getUnits();

    if (chars < 50)
      return 10;

    if (chars < 100)
      return 16;

    return 2 * chars;
  }

  public double cost(VoiceCommunication com) {
    double base = 20 * com.getUnits();

    if (com.getOriginTerminal().isFriend(com.getOriginTerminal().getId()))
      base *= 0.5;

    return base;
  }

  public double cost(VideoCommunication com) {
    double base = 30 * com.getUnits();

    if (com.getOriginTerminal().isFriend(com.getOriginTerminal().getId()))
      base *= 0.5;

    return base;
  }

  @Override
  public String toString() {
    return "NORMAL";
  }
}