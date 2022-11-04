package prr.notifications;

public class OffToIdle extends ClientNotification {
  public OffToIdle(String terminal_key) {
    super("O2I", terminal_key);
  }
}
