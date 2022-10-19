package prr.clients;

import java.io.Serializable;

import prr.terminals.Terminal;
import prr.database.AbstractModel;
import prr.database.TerminalCollection;

public class Client extends AbstractModel implements Serializable {
  private String name;
  private String nif;
  private TerminalCollection clientTerminals;

  public Client(String _id, String _name, String _nif) {
    this.id = _id;
    this.name = _name;
    this.nif = _nif;
  }

  public void addTerminal(Terminal terminal) {
    this.clientTerminals.insert(terminal);
  }

  public String getName() {
    return this.name;
  }

  public String getNif() {
    return this.nif;
  }
}
