package prr.models;

public class Terminal extends AbstractModel {
    private Client _client;

    public Client(String id, Client client) {
        _id = id;
        _client = client;
    }

    public Client getClient() {
        return _client;
    }
}
