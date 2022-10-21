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
    try {
      String terminalType = stringField("terminal_type");

      while (!(terminalType.equals("BASIC") || terminalType.equals("FANCY"))) {
        terminalType = Form.requestString(Prompt.terminalType());
      }

      String clientId = Form.requestString(Prompt.clientKey());
      String terminalId = stringField("terminal_id");

      Client client = _receiver.getClientsCollection().findById(clientId);

      _receiver.getTerminalsCollection().insert(terminalId, terminalType, client);
    } catch (prr.exceptions.InvalidTerminalKeyException e) {
      throw new InvalidTerminalKeyException(e.getKey());
    } catch (prr.exceptions.DuplicateTerminalKeyException e) {
      throw new DuplicateTerminalKeyException(e.getKey());
    } catch (prr.exceptions.UnknownClientKeyException e) {
      throw new UnknownClientKeyException(e.getKey());
    }
  }
}