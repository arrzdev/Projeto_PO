package prr.app.clients;

import prr.Network;
import prr.app.exceptions.DuplicateClientKeyException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Register new client.
 */
class DoRegisterClient extends Command<Network> {

  DoRegisterClient(Network receiver) {
    super(Label.REGISTER_CLIENT, receiver);
    addStringField("client_name", Prompt.name());
    addStringField("client_nif", Prompt.taxId());
  }

  @Override
  protected final void execute() throws CommandException {
    String name = stringField("client_name");
    String nif = stringField("client_nif");

    _receiver.getDB().getClientsCollection().insert(name, nif);
  }

}
