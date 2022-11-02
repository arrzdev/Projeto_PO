package prr.app.lookups;

import prr.Network;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import prr.app.exceptions.UnknownClientKeyException;

/**
 * Show communications to a client.
 */
class DoShowCommunicationsToClient extends Command<Network> {

  DoShowCommunicationsToClient(Network receiver) {
    super(Label.SHOW_COMMUNICATIONS_TO_CLIENT, receiver);
    addStringField("client_id", Prompt.clientKey());
  }

  @Override
  protected final void execute() throws CommandException {
    try {
      _receiver.showCommunicationReceived(stringField("client_id"));
    } catch (prr.exceptions.UnknownClientKeyException e) {
      throw new UnknownClientKeyException(e.getKey());
    }
  }
}
