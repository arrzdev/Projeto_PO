package prr.app.terminals;

import prr.Network;
import prr.app.exceptions.DuplicateTerminalKeyException;
import prr.app.exceptions.InvalidTerminalKeyException;
import prr.app.exceptions.UnknownClientKeyException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import prr.clients.Client;
import pt.tecnico.uilib.forms.Form;

/**
 * Register terminal.
 */
class DoRegisterTerminal extends Command<Network> {

  DoRegisterTerminal(Network receiver) {
    super(Label.REGISTER_TERMINAL, receiver);
    addStringField("terminal_id", Prompt.terminalKey());
    addStringField("terminal_type", Prompt.terminalType());
  }

  @Override
  protected final void execute() throws CommandException {
    String terminalType = stringField("terminal_type");

    while (!(terminalType.equals("BASIC") || terminalType.equals("FANCY"))) {
      terminalType = Form.requestString(Prompt.terminalType());
    }

    String clientId = Form.requestString(Prompt.clientKey());
    String terminalId = stringField("terminal_id");

    // TODO: fix implementation on frontend
    Client client = _receiver.getDB().getClientsCollection().findById(clientId);

    _receiver.getDB().getTerminalsCollection().insert(terminalId, terminalType, client);
  }
}
