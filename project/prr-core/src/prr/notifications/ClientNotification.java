package prr.notifications;

public abstract class ClientNotification {
  private String _notification_type;
  private String _terminal_key;

  public ClientNotification(String notification_type, String terminal_key) {
    _notification_type = notification_type;
    _terminal_key = terminal_key;
  }

  public String toString() {
    return String.format("%s|%s", _notification_type, _terminal_key);
  }
}
