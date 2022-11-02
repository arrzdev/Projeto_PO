package prr.payments;

import java.io.Serializable;
import prr.clients.Client;

import prr.communications.TextCommunication;
import prr.communications.VoiceCommunication;
import prr.communications.VideoCommunication;

public abstract class PaymentPlan implements Serializable {
  protected Client _client;

  public PaymentPlan(Client client) {
    _client = client;
  }

  protected abstract void update();

  public abstract void pay();

  public abstract int cost(TextCommunication com);

  public abstract int cost(VoiceCommunication com);

  public abstract int cost(VideoCommunication com);
}