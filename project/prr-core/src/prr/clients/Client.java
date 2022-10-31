package prr.clients;

import java.io.Serializable;
import java.util.TreeMap;

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
    return terminals.size();
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
      /* 
       JOAO FIQUEI NESTE TOPICO DO ENUNCIADO
       Mostrar informação sobre pagamentos e dívidas de cliente
       :)
       */
      client_payments += t.
    }
  }

  @Override
  public String toString() {
    String haveNotificationsEnabled = getNotificationsStatus() ? "YES" : "NO";

    return String.format("CLIENT|%s|%s|%s|NORMAL|%s|%d|0|0", getId(), getName(), getNif(), haveNotificationsEnabled,
        getNumberOfTerminals());
  }
}
