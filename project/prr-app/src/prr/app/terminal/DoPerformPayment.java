package prr.app.terminal;

import prr.Network;
import prr.terminals.Terminal;
import pt.tecnico.uilib.menus.CommandException;
// Add more imports if needed

/**
 * Perform payment.
 */
class DoPerformPayment extends TerminalCommand {

  DoPerformPayment(Network context, Terminal terminal) {
    super(Label.PERFORM_PAYMENT, context, terminal);
    addIntegerField("communication_id", Prompt.commKey());
  }

  @Override
  protected final void execute() throws CommandException {
    try {
      _network.performPayment(_receiver, integerField("communication_id"));
    } catch (prr.exceptions.UnknownCommunicationKeyException | prr.exceptions.BadPaymentException e) {
      _display.popup(Message.invalidCommunication());
    }
  }
}
