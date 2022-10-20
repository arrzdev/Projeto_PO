package prr.database;

import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;

import prr.database.AbstractModel;
import prr.exceptions.UnknownClientKeyException;
import prr.exceptions.UnknownTerminalKeyException;

import java.io.Serializable;

public abstract class AbstractCollection<T extends AbstractModel> implements Serializable {
  private Map<String, T> _data = new TreeMap<String, T>();

  public Map<String, T> getData() {
    return _data;
  }

  public void insert(T item) {
    _data.put(item.getId(), item);
  }

  public void remove(T item) {
    _data.remove(item.getId());
  }

  public void update(T item) {
    _data.put(item.getId(), item);
  }

  public int size() {
    return _data.size();
  }

  // TODO: implement sorted list when return
  public ArrayList<T> findAll() {
    ArrayList<T> all = new ArrayList<T>();

    for (T v : _data.values()) {
      all.add(v);
    }

    return all;
  }
}
