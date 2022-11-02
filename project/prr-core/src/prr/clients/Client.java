package prr.clients;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;

import prr.communications.Communication;
import prr.terminals.Terminal;

public class Client implements Serializable {
  private String _id;
  private String _name;
  private int _nif;
  private boolean _notifications = true;

  private TreeMap<String, Terminal> _terminals = new TreeMap<String, Terminal>();

  public Client(String id, String name, int nif) {
    _id = id;
    _name = name;
    // _nif = nif.replaceFirst("^0+(?!$)", "");
    _nif = nif;
  }

  public void addTerminal(Terminal terminal) {
    _terminals.put(terminal.getId(), terminal);
  }

  public String getId() {
    return _id;
  }

  public String getName() {
    return _name;
  }

  public int getNif() {
    return _nif;
  }

  public int getNumberOfTerminals() {
    return _terminals.size();
  }

  public void toggleNotifications() {
    // change notifications status between true and false
    if (getNotificationsStatus()) {
      _notifications = false;
    } else {
      _notifications = true;
    }
  }

  public boolean getNotificationsStatus() {
    return _notifications;
  }

  public long getClientPayments() {
    long client_payments = 0;

    for (Terminal t : _terminals.values()) {
      client_payments += t.getPayments();
    }

    return client_payments;
  }

  public long getClientDebts() {
    long client_debts = 0;

    for (Terminal t : _terminals.values()) {
      client_debts += t.getDebts();
    }

    return client_debts;
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
    String haveNotificationsEnabled = getNotificationsStatus() ? "YES" : "NO";

    return String.format("CLIENT|%s|%s|%s|NORMAL|%s|%d|0|0", getId(), getName(), getNif(), haveNotificationsEnabled,
        getNumberOfTerminals());
  }
}
