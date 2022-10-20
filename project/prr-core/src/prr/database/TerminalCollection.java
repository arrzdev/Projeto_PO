package prr.database;

import prr.terminals.Terminal;
import prr.terminals.FancyTerminal;
import prr.terminals.IdleState;

import prr.clients.Client;

import java.io.Serializable;
import java.util.ArrayList;

import prr.exceptions.UnknownTerminalKeyException;
import prr.exceptions.DuplicateTerminalKeyException;

public class TerminalCollection extends AbstractCollection<Terminal> implements Serializable {
  public Terminal findByClient(String clientId) {
    for (Terminal terminal : getData().values()) {
      if (terminal.getClient().getId().equals(clientId)) {
        return terminal;
      }
    }

    return null;
  }

  /*
   * First register default model on the collection and then register on the
   * client Object TreeMap
   */
  @Override
  public void insert(Terminal terminal) {
    super.insert(terminal);

    Client client = terminal.getClient();

    // insert the terminal into the clients
    client.addTerminal(terminal);
  }

  public void insert(String terminalId, String terminalType, Client client) throws DuplicateTerminalKeyException {
    // Check if Terminal exists in treeMap _data
    if (getData().get(terminalId) != null) {
      throw new DuplicateTerminalKeyException(terminalId);
    }

    Terminal terminal;
    if (terminalType.equals("BASIC")) {
      terminal = new Terminal(terminalId, client);
    } else {
      terminal = new FancyTerminal(terminalId, client);
    }

    terminal.setTerminalState(new IdleState(terminal));

    insert(terminal);
  }

  public Terminal findById(String id) throws UnknownTerminalKeyException {
    Terminal t = getData().get(id);

    if (t == null) {
      throw new UnknownTerminalKeyException(id);
    }

    return t;
  }

  public ArrayList<Terminal> findInactive() {
    ArrayList<Terminal> inactive = new ArrayList<Terminal>();

    for (Terminal t : getData().values()) {
      if (t.totalCommunicationsCount() == 0)
        inactive.add(t);
    }

    return inactive;
  }
}
