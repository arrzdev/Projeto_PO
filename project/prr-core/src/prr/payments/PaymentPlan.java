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
    double total = 0;
    switch (com.getType()) {
      case "TEXT" -> total = cost((TextCommunication) com);
      case "VOICE" -> total = cost((VoiceCommunication) com);
      case "VIDEO" -> total = cost((VideoCommunication) com);
    }

    return total;
  }

  public abstract double cost(TextCommunication com);

  public abstract double cost(VoiceCommunication com);

  public abstract double cost(VideoCommunication com);
}