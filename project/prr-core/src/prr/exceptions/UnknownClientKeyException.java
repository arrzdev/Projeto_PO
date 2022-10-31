package prr.exceptions;

public class UnknownClientKeyException extends Exception {
  private static final long serialVersionUID = 202110232154L;

  private String _key;

  /** @param key Unknown key provided. */
  public UnknownClientKeyException(String key) {
    super();
    _key = key;
  }

  /**
   * @param key   Unknown key provided.
   * @param cause What exception caused this one.
   */
  public UnknownClientKeyException(String key, Exception cause) {
    super(cause);
    _key = key;
  }

  /**
   * @return the key
   */
  public String getKey() {
    return _key;
  }
}