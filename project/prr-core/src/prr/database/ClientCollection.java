package prr.database;

import prr.clients.Client;

public class ClientCollection extends AbstractCollection<Client> {
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
