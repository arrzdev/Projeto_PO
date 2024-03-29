package prr.app.clients;

import prr.Network;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import java.util.ArrayList;
import prr.clients.Client;

/**
 * Show all clients.
 */
class DoShowAllClients extends Command<Network> {

  DoShowAllClients(Network receiver) {
    super(Label.SHOW_ALL_CLIENTS, receiver);
  }

  @Override
  protected final void execute() throws CommandException {
    _display.addAll(_receiver.getClients());
    _display.display();
  }
}
