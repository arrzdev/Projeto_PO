package prr.notifications;

public class SilentToIdle extends ClientNotification {
  public SilentToIdle(String terminal_key) {
    super("S2I", terminal_key);
  }
}
