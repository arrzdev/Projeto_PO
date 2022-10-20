package prr.app.clients;

import prr.clients.Client;
import prr.Network;
import prr.app.exceptions.UnknownClientKeyException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show specific client: also show previous notifications.
 */
class DoShowClient extends Command<Network> {

  DoShowClient(Network receiver) {
    super(Label.SHOW_CLIENT, receiver);
    addStringField("client_id", Prompt.key());
  }

  @Override
  protected final void execute() throws CommandException {
    Client client = _receiver.getDB().getClientsCollection().findById(stringField("client_id"));

    if (client == null) {
      throw new UnknownClientKeyException(stringField("client_id"));
    }

    _display.addLine(client);
    _display.display();
  }
}
