package prr.payments;

import java.io.Serializable;
import prr.clients.Client;

import prr.communications.TextCommunication;
import prr.communications.VideoCommunication;
import prr.communications.VoiceCommunication;

public class GoldPaymentPlan extends PaymentPlan implements Serializable {
  private int videoComsMadeWithPositiveBalance = 0;

  public GoldPaymentPlan(Client c) {
    super(c);
  }

  protected void update() {
    if (_client.getBalance() < 0)
      _client.setPaymentPlan(new NormalPaymentPlan(_client));

    if (videoComsMadeWithPositiveBalance == 5)
      _client.setPaymentPlan(new PlatiniumPaymentPlan(_client));

  }

  public void pay() {
    // TODO: update number of video communication made with positive balance

    update();
  }

  public int cost(TextCommunication com) {
    int chars = com.getMessage().length();

    if (chars < 50)
      return 10;

    if (chars < 100)
      return 10;

    return 2 * chars;
  }

  public int cost(VoiceCommunication com) {
    int base = 10;

    if (com.getSender().isFriend(com.getSender().getId()))
      base *= 0.5;

    return base;
  }

  public int cost(VideoCommunication com) {
    int base = 20;

    if (com.getSender().isFriend(com.getSender().getId()))
      base *= 0.5;

    return base;
  }
}