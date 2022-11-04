package prr.app.clients;

import javax.sound.midi.Receiver;

import prr.Network;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import prr.app.exceptions.UnknownClientKeyException;

/**
 * Disable client notifications.
 */
class DoDisableClientNotifications extends Command<Network> {

  DoDisableClientNotifications(Network receiver) {
    super(Label.DISABLE_CLIENT_NOTIFICATIONS, receiver);
    addStringField("client_id", Prompt.key());
  }

  @Override
  protected final void execute() throws CommandException {
    try {
      _receiver.disableClientNotifications(stringField("client_id"));
    } catch (prr.exceptions.UnknownClientKeyException e) {
      throw new UnknownClientKeyException(e.getKey());
    } catch (prr.exceptions.ClientNotificationsAlreadyDisabledException e) {
      _display.popup(Message.clientNotificationsAlreadyDisabled());
    }
  }
}
