package prr.terminals;

import prr.clients.Client;

import java.io.Serializable;

public class FancyTerminal extends Terminal implements Serializable {
  public FancyTerminal(String id, Client client) {
    super(id, client);
  }

  @Override
  public String getTerminalType() {
    return "FANCY";
  }

}
