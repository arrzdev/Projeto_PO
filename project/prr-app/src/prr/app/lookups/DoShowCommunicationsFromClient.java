package prr.app.lookups;

import prr.Network;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import prr.app.exceptions.UnknownClientKeyException;
//FIXME add more imports if needed

/**
 * Show communications from a client.
 */
class DoShowCommunicationsFromClient extends Command<Network> {

  DoShowCommunicationsFromClient(Network receiver) {
    super(Label.SHOW_COMMUNICATIONS_FROM_CLIENT, receiver);
    addStringField("client_id", Prompt.clientKey());
  }

  @Override
  protected final void execute() throws CommandException {
    try {
      _receiver.showCommunicationSent(stringField("client_id"));
    } catch (prr.exceptions.UnknownClientKeyException e) {
      throw new UnknownClientKeyException(e.getKey());
    }
  }
}
