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
    addOptionField("terminal_type", Prompt.terminalType(), new String[] { "BASIC", "FANCY" });
    addStringField("client_key", Prompt.clientKey());
  }

  @Override
  protected final void execute() throws CommandException {
    try {
      _receiver.registerTerminal(stringField("terminal_id"), optionField("terminal_type"), stringField("client_key"));
    } catch (prr.exceptions.InvalidTerminalKeyException e) {
      throw new InvalidTerminalKeyException(e.getKey());
    } catch (prr.exceptions.DuplicateTerminalKeyException e) {
      throw new DuplicateTerminalKeyException(e.getKey());
    } catch (prr.exceptions.UnknownClientKeyException e) {
      throw new UnknownClientKeyException(e.getKey());
    }
  }
}