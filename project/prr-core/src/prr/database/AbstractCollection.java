package prr.database;

import java.util.Map;
import java.util.TreeMap;

import prr.database.AbstractModel;

public abstract class AbstractCollection<T extends AbstractModel> {
    private Map<String, T> _data = new TreeMap<String, T>();

    public Map<String, T> getData() {
        return _data;
    }

    public void register(T item) {
        _data.put(item.getId(), item);
    }

    public T findById(String id) {
        return _data.get(id);
    }

    public void remove(T item) {
        _data.remove(item.getId());
    }

    public void update(T item) {
        _data.put(item.getId(), item);
    }

}
