package prr.notifications;

public class OffToSilent extends ClientNotification {
  public OffToSilent(String terminal_key) {
    super("O2S", terminal_key);
  }
}
