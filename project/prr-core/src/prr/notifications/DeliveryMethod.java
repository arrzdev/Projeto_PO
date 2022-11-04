package prr.notifications;

import prr.notifications.CustomNotification;

public interface DeliveryMethod {
    public void deliver(CustomNotification notification);
}
