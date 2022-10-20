package prr.exceptions;

public class DuplicateTerminalKeyException extends Exception {

  private static final long serialVersionUID = 202110291053L;

  private String _key;

  public DuplicateTerminalKeyException(String key) {
    _key = key;
  }

  public String getKey() {
    return _key;
  }
}