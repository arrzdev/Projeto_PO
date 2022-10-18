package prr.models;

public class Client extends AbstractModel {
    private String _name;
    private String _nif;

    public Client(String id, String name, String nif) {
        _id = id;
        _name = name;
        _nif = nif;
    }

    public String getName() {
        return _name;
    }

    public String getNif() {
        return _nif;
    }
}
