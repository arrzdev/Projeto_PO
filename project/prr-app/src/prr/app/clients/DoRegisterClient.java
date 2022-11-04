package prr.app.clients;

import prr.Network;
import prr.app.exceptions.DuplicateClientKeyException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Register new client.
 */
class DoRegisterClient extends Command<Network> {

  DoRegisterClient(Network receiver) {
    super(Label.REGISTER_CLIENT, receiver);
    addStringField("client_id", Prompt.key());
    addStringField("client_name", Prompt.name());
    addStringField("client_nif", Prompt.taxId());
  }

  @Override
  protected final void execute() throws CommandException {
    try {
      String id = stringField("client_id");
      String name = stringField("client_name");
      int nif = Integer.parseInt(stringField("client_nif"));

      _receiver.registerClient(id, name, nif);
    } catch (prr.exceptions.DuplicateClientKeyException e) {
      throw new DuplicateClientKeyException(e.getKey());
    }
  }

}
