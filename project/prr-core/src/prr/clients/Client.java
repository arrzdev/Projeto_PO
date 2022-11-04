package prr.clients;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;

import prr.communications.Communication;
import prr.notifications.ClientNotification;
import prr.terminals.Terminal;
import prr.payments.PaymentPlan;
import prr.payments.NormalPaymentPlan;

public class Client implements Serializable {
  private String _id;
  private String _name;
  private int _nif;
  private boolean receive_notifications = true;
  private ArrayList<ClientNotification> _notifications = new ArrayList<ClientNotification>();

  private TreeMap<String, Terminal> _terminals = new TreeMap<String, Terminal>();
  private PaymentPlan _paymentPlan = new NormalPaymentPlan(this);

  public Client(String id, String name, int nif) {
    _id = id;
    _name = name;
    _nif = nif;
  }

  public String getId() {
    return _id;
  }

  public void addNotification(ClientNotification notification) {
    _notifications.add(notification);
  }

  public void clearNotifications() {
    _notifications.clear();
  }

  public ArrayList<ClientNotification> getNotifications() {
    return _notifications;
  }

  public void setPaymentPlan(PaymentPlan paymentPlan) {
    _paymentPlan = paymentPlan;
  }

  public void addTerminal(Terminal terminal) {
    _terminals.put(terminal.getId(), terminal);
  }

  public void toggleReceiveNotifications() {
    // change notifications status between true and false
    if (canReceiveNotifications()) {
      receive_notifications = false;
    } else {
      receive_notifications = true;
    }
  }

  public boolean canReceiveNotifications() {
    return receive_notifications;
  }

  public double getPayments() {
    double client_payments = 0;

    for (Terminal t : _terminals.values()) {
      client_payments += t.getPayments();
    }

    return Math.round(client_payments);
  }

  public double getDebts() {
    double client_debts = 0;

    for (Terminal t : _terminals.values()) {
      client_debts += t.getDebts();
    }

    return Math.round(client_debts);
  }

  public int getBalance() {
    return (int) (getPayments() - getDebts());
  }

  public PaymentPlan getPaymentPlan() {
    return _paymentPlan;
  }

  public ArrayList<Communication> getSentCommunications() {
    ArrayList<Communication> comms = new ArrayList<Communication>();

    for (Terminal t : _terminals.values()) {
      comms.addAll(t.getSentComms());
    }

    comms.sort(Comparator.comparing(Communication::getId));

    return comms;
  }

  public ArrayList<Communication> getReceivedCommunications() {
    ArrayList<Communication> comms = new ArrayList<Communication>();

    for (Terminal t : _terminals.values()) {
      comms.addAll(t.getReceivedComms());
    }

    comms.sort(Comparator.comparing(Communication::getId));

    return comms;
  }

  @Override
  public String toString() {
    String hasNotificationsEnabled = canReceiveNotifications() ? "YES" : "NO";

    return String.format("CLIENT|%s|%s|%s|%s|%s|%d|%d|%d", _id, _name, _nif, _paymentPlan, hasNotificationsEnabled,
        _terminals.size(), Math.round(getPayments()), Math.round(getDebts()));
  }
}
