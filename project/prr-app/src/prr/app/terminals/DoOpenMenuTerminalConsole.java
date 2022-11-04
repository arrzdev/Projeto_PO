package prr.app.terminals;

import prr.Network;
import prr.app.terminal.Menu;
import prr.terminals.Terminal;
import prr.app.exceptions.UnknownTerminalKeyException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Open a specific terminal's menu.
 */
class DoOpenMenuTerminalConsole extends Command<Network> {

  DoOpenMenuTerminalConsole(Network receiver) {
    super(Label.OPEN_MENU_TERMINAL, receiver);
    // receive terminal number
    addStringField("terminal_id", Prompt.terminalKey());
  }

  @Override
  protected final void execute() throws CommandException {
    try {
      Terminal terminal = _receiver.getTerminal(stringField("terminal_id"));
      Menu menu = new Menu(_receiver, terminal);
      menu.open();
    } catch (prr.exceptions.UnknownTerminalKeyException e) {
      throw new UnknownTerminalKeyException(e.getKey());
    }
  }
}
