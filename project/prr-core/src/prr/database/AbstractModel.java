package prr.database;

import java.io.Serializable;

public abstract class AbstractModel implements Serializable {
  protected String _id;

  public String getId() {
    return _id;
  }
}
