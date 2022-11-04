package prr.payments;

import java.io.Serializable;
import prr.clients.Client;

import prr.communications.TextCommunication;
import prr.communications.VoiceCommunication;
import prr.communications.VideoCommunication;
import prr.communications.Communication;

public abstract class PaymentPlan implements Serializable {
  protected Client _client;

  private int _streak = 0;

  public PaymentPlan(Client client) {
    _client = client;
  }

  public int getStreak() {
    return _streak;
  }

  public void increaseStreak() {
    _streak += 1;
  }

  public void resetStreak() {
    _streak = 0;
  }

  public abstract void update();

  public double cost(Communication com) {
    switch (com.getType()) {
      case "TEXT" -> cost((TextCommunication) com);
      case "VOICE" -> cost((VoiceCommunication) com);
      case "VIDEO" -> cost((VideoCommunication) com);
    }

    return 0;
  }

  public abstract double cost(TextCommunication com);

  public abstract double cost(VoiceCommunication com);

  public abstract double cost(VideoCommunication com);
}