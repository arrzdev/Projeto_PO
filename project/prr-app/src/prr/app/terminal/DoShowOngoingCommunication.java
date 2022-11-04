package prr.app.terminal;

import prr.Network;
import prr.communications.Communication;
import prr.terminals.Terminal;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Command for showing the ongoing communication.
 */
class DoShowOngoingCommunication extends TerminalCommand {

  DoShowOngoingCommunication(Network context, Terminal terminal) {
    super(Label.SHOW_ONGOING_COMMUNICATION, context, terminal);
  }

  @Override
  protected final void execute() throws CommandException {
    try {
      _display.popup(_network.getCurrentCommunication(_receiver));
    } catch (prr.exceptions.NoCommunicationException e) {
      _display.popup(Message.noOngoingCommunication());
    }
  }
}
