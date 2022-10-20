package prr.exceptions;

public class UnknownTerminalKeyException extends Exception {
  private String _key;

  private static final long serialVersionUID = 202208091753L;

  /** @param key Unknown key provided. */
  public UnknownTerminalKeyException(String key) {
    super();
    _key = key;
  }

  /**
   * @param key   Unknown key provided.
   * @param cause What exception caused this one.
   */
  public UnknownTerminalKeyException(String key, Exception cause) {
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
