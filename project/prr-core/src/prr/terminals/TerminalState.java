package prr.terminals;

import javax.swing.AbstractAction;

import java.io.Serializable;

public abstract class TerminalState implements Serializable {
  protected Terminal _terminal;

  public TerminalState(Terminal t) {
    _terminal = t;
  }

  public boolean allowReceiveText() {
    return true;
  }

  public boolean allowSendText() {
    return true;
  }

  public boolean allowReceiveVideo() {
    return _terminal instanceof FancyTerminal;
  }

  public boolean allowSendVideo() {
    return _terminal instanceof FancyTerminal;
  }
}
