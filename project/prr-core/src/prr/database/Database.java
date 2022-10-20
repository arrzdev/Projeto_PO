package prr.database;

import java.io.Serializable;

public class Database implements Serializable {
  private ClientCollection _clients = new ClientCollection();
  private TerminalCollection _terminals = new TerminalCollection();

  public ClientCollection getClientsCollection() {
    return _clients;
  }

  public TerminalCollection getTerminalsCollection() {
    return _terminals;
  }
}
