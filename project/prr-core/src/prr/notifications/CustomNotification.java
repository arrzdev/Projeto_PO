package prr.notifications;

import java.io.Serializable;

import prr.terminals.Terminal;

public class CustomNotification implements Serializable {
    private Terminal _from;
    private Terminal _to;
    private boolean _delivered = false;
    private String _notificationType;

    public DeliveryMethod _deliveryMethod;

    public CustomNotification(Terminal from, Terminal to, String notificationType, DeliveryMethod deliveryMethod) {
        _from = from;
        _to = to;
        _deliveryMethod = deliveryMethod;
        _notificationType = notificationType;

        deliver();
    }

    public String getNotificationType() {
        return _notificationType;
    }

    public Terminal getFrom() {
        return _from;
    }

    public Terminal getTo() {
        return _to;
    }

    public boolean isDelivered() {
        return _delivered;
    }

    public void deliver() {
        _delivered = true;
        _deliveryMethod.deliver(this);
    }
}
