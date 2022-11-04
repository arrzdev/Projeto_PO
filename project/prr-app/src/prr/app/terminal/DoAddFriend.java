package prr.app.terminal;

import prr.Network;
import prr.app.exceptions.UnknownTerminalKeyException;
import prr.terminals.Terminal;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Add a friend.
 */
class DoAddFriend extends TerminalCommand {

  DoAddFriend(Network context, Terminal terminal) {
    super(Label.ADD_FRIEND, context, terminal);
    addStringField("terminal_id", Prompt.terminalKey());
  }

  @Override
  protected final void execute() throws CommandException {
    try {
      _network.addFriend(_receiver, stringField("terminal_id"));
    } catch (prr.exceptions.UnknownTerminalKeyException e) {
      throw new UnknownTerminalKeyException(e.getKey());
    }
  }
}
