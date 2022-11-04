package prr.notifications;

public class BusyToIdle extends ClientNotification {
  public BusyToIdle(String terminal_key) {
    super("B2I", terminal_key);
  }
}
