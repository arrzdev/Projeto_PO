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

  protected void update() {
    if (_client.getBalance() > 500)
      _client.setPaymentPlan(new GoldPaymentPlan(_client));
  }

  public void pay() {
    update();
  }

  public int cost(TextCommunication com) {
    int chars = com.getMessage().length();

    if (chars < 50)
      return 10;

    if (chars < 100)
      return 16;

    return 2 * chars;
  }

  public int cost(VoiceCommunication com) {
    int base = 20;

    if (com.getSender().isFriend(com.getSender().getId()))
      base *= 0.5;

    return base;
  }

  public int cost(VideoCommunication com) {
    int base = 30;

    if (com.getSender().isFriend(com.getSender().getId()))
      base *= 0.5;

    return base;
  }
}