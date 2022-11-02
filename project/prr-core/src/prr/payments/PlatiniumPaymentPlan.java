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

  protected void update() {
    if (_client.getBalance() < 0)
      _client.setPaymentPlan(new NormalPaymentPlan(_client));
  }

  public void pay() {
    update();
  }

  public int cost(TextCommunication com) {
    int chars = com.getMessage().length();

    if (chars < 50)
      return 0;

    if (chars < 100)
      return 4;

    return 4;
  }

  public int cost(VoiceCommunication com) {
    int base = 10;

    if (com.getSender().isFriend(com.getSender().getId()))
      base *= 0.5;

    return base;
  }

  public int cost(VideoCommunication com) {
    int base = 10;

    if (com.getSender().isFriend(com.getSender().getId()))
      base *= 0.5;

    return base;
  }
}