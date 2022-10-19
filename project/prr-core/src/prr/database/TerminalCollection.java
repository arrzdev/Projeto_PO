package prr.database;

import prr.terminals.Terminal;

public class TerminalCollection extends AbstractCollection<Terminal> {
  public Terminal findByClient(String clientId) {
    for (Terminal terminal : getData().values()) {
      if (terminal.getClient().getId().equals(clientId)) {
        return terminal;
      }
    }

    return null;
  }
}
