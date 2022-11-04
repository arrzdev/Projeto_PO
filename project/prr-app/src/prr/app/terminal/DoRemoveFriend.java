package prr.app.terminal;

import prr.Network;
import prr.terminals.Terminal;
import pt.tecnico.uilib.menus.CommandException;

import prr.app.exceptions.UnknownTerminalKeyException;

/**
 * Remove friend.
 */
class DoRemoveFriend extends TerminalCommand {

  DoRemoveFriend(Network context, Terminal terminal) {
    super(Label.REMOVE_FRIEND, context, terminal);
    addStringField("terminal_id", Prompt.terminalKey());
  }

  @Override
  protected final void execute() throws CommandException {
    try {
      _network.removeFriend(_receiver, stringField("terminal_id"));
    } catch (prr.exceptions.UnknownTerminalKeyException e) {
      throw new UnknownTerminalKeyException(e.getKey());
    }
  }
}
