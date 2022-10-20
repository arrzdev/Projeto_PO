package prr.database;

import prr.clients.Client;

import java.io.Serializable;

import prr.app.exceptions.DuplicateClientKeyException;

public class ClientCollection extends AbstractCollection<Client> implements Serializable {
  public void insert(String name, String nif) throws DuplicateClientKeyException {
    int clientId = getNextId();

    String id = String.format("cli%03d", clientId);

    if (clientId > 999 || findById(id) != null) {
      throw new DuplicateClientKeyException("Client key already exists");
    }

    Client cli = new Client(id, name, nif);
    insert(cli);
  }

  public int getNextId() {
    return this.size() + 1;
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
