package prr.terminals;

import prr.clients.Client;

public class FancyTerminal extends Terminal {
  FancyTerminal(String id, Client client, TerminalState state) {
    super(id, client, state);
  }
}
