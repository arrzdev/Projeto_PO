package prr.notifications;

import java.io.Serializable;
import prr.notifications.CustomNotification;
import prr.terminals.Terminal;

public class AppNotification implements DeliveryMethod, Serializable {
    public void deliver(CustomNotification notification) {
        notification.getTo().pushNotification(notification);
    }
}
