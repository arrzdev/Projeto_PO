package prr.clients;

import java.io.Serializable;
import java.util.TreeMap;

import prr.terminals.Terminal;
import prr.database.AbstractModel;
import prr.database.TerminalCollection;

public class Client extends AbstractModel implements Serializable {
  private String _name;
  private int _nif;
  private TreeMap<String, Terminal> terminals = new TreeMap<String, Terminal>();

  public Client(String id, String name, int nif) {
    _id = id;
    _name = name;
    // _nif = nif.replaceFirst("^0+(?!$)", "");
    _nif = nif;
  }

  public void addTerminal(Terminal terminal) {
    terminals.put(terminal.getId(), terminal);
  }

  public void removeTerminal(Terminal terminal) {
    removeTerminal(terminal.getId());
  }

  public void removeTerminal(String terminalId) {
    terminals.remove(terminalId);
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

  @Override
  public String toString() {
    return String.format("CLIENT|%s|%s|%s|NORMAL|YES|%d|0|0", getId(), getName(), getNif(), getNumberOfTerminals());
  }
}
