package prr.app.terminal;

import prr.Network;
import prr.terminals.Terminal;
import prr.app.exceptions.UnknownTerminalKeyException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Command for sending a text communication.
 */
class DoSendTextCommunication extends TerminalCommand {

  DoSendTextCommunication(Network context, Terminal terminal) {
    super(Label.SEND_TEXT_COMMUNICATION, context, terminal, receiver -> receiver.canStartCommunication());
    addStringField("destination_id", Prompt.terminalKey());
    addStringField("message_body", Prompt.textMessage());
  }

  @Override
  protected final void execute() throws CommandException {
    try {
      _network.sendTextCommunication(_receiver, stringField("destination_id"), stringField("message_body"));
    } catch (prr.exceptions.UnknownTerminalKeyException e) {
      throw new UnknownTerminalKeyException(e.getKey());
    } catch (prr.exceptions.DestinationIsOffException e) {
      _display.popup(Message.destinationIsOff(stringField("destination_id")));
    }
  }
}
