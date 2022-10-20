package prr.database;

import prr.clients.Client;
import prr.exceptions.UnknownClientKeyException;
import prr.exceptions.UnknownTerminalKeyException;
import prr.exceptions.DuplicateClientKeyException;
import java.io.Serializable;

public class ClientCollection extends AbstractCollection<Client> implements Serializable {
  public void insert(String id, String name, String nif) throws DuplicateClientKeyException {
    // Check if Client exists in treeMap _data
    if (getData().get(id) != null) {
      throw new DuplicateClientKeyException(id);
    }

    Client cli = new Client(id, name, nif);
    insert(cli);
  }

  // TODO: check exception thrown APP
  public Client findById(String id) throws UnknownClientKeyException {
    Client cli = getData().get(id);

    if (cli == null) {
      throw new UnknownClientKeyException(id);
    }

    return cli;
  }

  public Client findByName(String name) {
    for (Client client : getData().values()) {
      if (client.getName().equals(name)) {
        return client;
      }
    }

    return null;
  }

  public Client findByNif(String nif) {
    for (Client client : getData().values()) {
      if (client.getNif().equals(nif)) {
        return client;
      }
    }

    return null;
  }
}
