package prr.app.clients;

import prr.Network;
import prr.app.exceptions.UnknownClientKeyException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Show the payments and debts of a client.
 */
class DoShowClientPaymentsAndDebts extends Command<Network> {

  DoShowClientPaymentsAndDebts(Network receiver) {
    super(Label.SHOW_CLIENT_BALANCE, receiver);
    addStringField("client_id", Prompt.key());
  }

  @Override
  protected final void execute() throws CommandException {
    try {
      long[] paymentsAndDebts = _receiver.getClientPaymentsAndDebts(stringField("client_id"));
      long client_payments = paymentsAndDebts[0];
      long client_debts = paymentsAndDebts[1];

      _display.popup(
          Message.clientPaymentsAndDebts(stringField("client_id"), client_payments, client_debts));

    } catch (prr.exceptions.UnknownClientKeyException e) {
      throw new UnknownClientKeyException(e.getKey());
    }

  }
}
