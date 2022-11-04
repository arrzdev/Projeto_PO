package prr.app.terminal;

import prr.Network;
import prr.app.exceptions.UnknownTerminalKeyException;
import prr.terminals.Terminal;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Command for starting communication.
 */
class DoStartInteractiveCommunication extends TerminalCommand {

  DoStartInteractiveCommunication(Network context, Terminal terminal) {
    super(Label.START_INTERACTIVE_COMMUNICATION, context, terminal, receiver -> receiver.canStartCommunication());
    addStringField("destination_id", Prompt.terminalKey());
    addOptionField("communication_type", Prompt.commType(), new String[] { "VOICE", "VIDEO" });
  }

  @Override
  protected final void execute() throws CommandException {
    try {
      _network.startInteractiveCommunication(_receiver, stringField("destination_id"),
          optionField("communication_type"));
    } catch (prr.exceptions.UnknownTerminalKeyException e) {
      throw new UnknownTerminalKeyException(e.getKey());
    } catch (prr.exceptions.UnsupportedAtOriginException e) {
      _display.popup(Message.unsupportedAtOrigin(stringField("destination_id"), optionField("communication_type")));
    } catch (prr.exceptions.UnsupportedAtDestinationException e) {
      _display
          .popup(Message.unsupportedAtDestination(stringField("destination_id"), optionField("communication_type")));
    } catch (prr.exceptions.DestinationIsOffException e) {
      _display.popup(Message.destinationIsOff(stringField("destination_id")));
    } catch (prr.exceptions.DestinationIsBusyException e) {
      _display.popup(Message.destinationIsBusy(stringField("destination_id")));
    } catch (prr.exceptions.DestinationIsSilentException e) {
      _display.popup(Message.destinationIsSilent(stringField("destination_id")));
    }
  }
}
