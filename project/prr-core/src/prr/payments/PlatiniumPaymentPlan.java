package prr.payments;

import java.io.Serializable;
import prr.clients.Client;

import prr.communications.TextCommunication;
import prr.communications.VideoCommunication;
import prr.communications.VoiceCommunication;

public class PlatiniumPaymentPlan extends PaymentPlan implements Serializable {
  public PlatiniumPaymentPlan(Client c) {
    super(c);
  }

  public void update() {
    if (_client.getBalance() < 0)
      _client.setPaymentPlan(new NormalPaymentPlan(_client));
  }

  public double cost(TextCommunication com) {
    double chars = com.getUnits();

    if (chars < 50)
      return 0;

    if (chars < 100)
      return 4;

    return 4;
  }

  public double cost(VoiceCommunication com) {
    double base = 10 * com.getUnits();

    if (com.getSender().isFriend(com.getSender().getId()))
      base *= 0.5;

    return base;
  }

  public double cost(VideoCommunication com) {
    double base = 10 * com.getUnits();

    if (com.getSender().isFriend(com.getSender().getId()))
      base *= 0.5;

    return base;
  }

  @Override
  public String toString() {
    return "PLATINIUM";
  }
}