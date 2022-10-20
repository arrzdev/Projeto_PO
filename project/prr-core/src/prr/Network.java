package prr;

import java.io.Serializable;

import prr.database.ClientCollection;
import prr.database.TerminalCollection;

import java.io.BufferedReader;
import java.io.FileReader;

//import exceptions
import java.io.IOException;
import prr.exceptions.UnrecognizedEntryException;

//clients
import prr.clients.Client;

//terminals
import prr.terminals.Terminal;
import prr.terminals.BusyState;

import prr.terminals.TerminalState;
import prr.terminals.IdleState;
import prr.terminals.SilenceState;
import prr.terminals.FancyTerminal;
import prr.terminals.OffState;

/**
 * Class Store implements a store.
 */
public class Network implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202208091753L;

  private ClientCollection _clients = new ClientCollection();
  private TerminalCollection _terminals = new TerminalCollection();

  public ClientCollection getClientsCollection() {
    return _clients;
  }

  public TerminalCollection getTerminalsCollection() {
    return _terminals;
  }

  /**
   * Read text input file and create corresponding domain entities.
   * 
   * @param filename name of the text input file
   * @throws UnrecognizedEntryException if some entry is not correct
   * @throws IOException                if there is an IO erro while processing
   *                                    the text file
   */
  void importFile(String filename) throws UnrecognizedEntryException, IOException /* FIXME add exception */ {
    // load file from system
    try {
      BufferedReader s = new BufferedReader(new FileReader(filename));

      String line;
      while ((line = s.readLine()) != null) {
        String[] parsedCommand = line.split("\\|");
        String commandType = parsedCommand[0];
        switch (commandType) {
          case "CLIENT" -> processClient(parsedCommand);
          case "BASIC", "FANCY" -> processTerminal(parsedCommand);
          case "FRIENDS" -> processFriends(parsedCommand);
          default -> throw new UnrecognizedEntryException(String.join("|", parsedCommand));
        }
      }
      s.close();
    } catch (UnrecognizedEntryException e) {
      throw new UnrecognizedEntryException("File not found");
    }
  }

  /*
   * Process a client entry.
   * 
   * @param parsedCommand fields of the entry
   * 
   * @throws UnrecognizedEntryException if the entry is not correct
   */
  private void processClient(String[] parsedCommand) throws UnrecognizedEntryException {
    if (parsedCommand.length != 4) {
      throw new UnrecognizedEntryException("Invalid number of arguments");
    }

    try {
      String id = parsedCommand[1];
      String name = parsedCommand[2];
      String nif = parsedCommand[3];

      // TODO: ?? call a checker method on the id, name and nif
      // TODO: throw exception in case of invalid parameters
      Client client = new Client(id, name, nif);
      _clients.insert(client);

    } catch (Exception e) {
      throw new UnrecognizedEntryException("Error while processing client");
    }
  }

  /*
   * Process a terminal entry.
   * 
   * @param parsedCommand fields of the entry
   * 
   * @throws UnrecognizedEntryException if the entry is not correct
   */
  private void processTerminal(String[] parsedCommand) throws UnrecognizedEntryException {
    if (parsedCommand.length != 4) {
      throw new UnrecognizedEntryException("Invalid number of arguments");
    }

    try {
      String terminal_type = parsedCommand[0];

      String id = parsedCommand[1];
      String clientId = parsedCommand[2];
      String terminalStateName = parsedCommand[3];

      Client client = _clients.findById(clientId);
      TerminalState terminalState;

      // TODO: ?? call a checker method on the id, name and nif
      // TODO: throw exception in case of invalid parameters
      Terminal terminal;
      if (terminal_type.equals("BASIC")) {
        terminal = new Terminal(id, client);
      } else {
        terminal = new FancyTerminal(id, client);
      }

      switch (terminalStateName) {
        case "ON" -> terminalState = new IdleState(terminal);
        case "OFF" -> terminalState = new OffState(terminal);
        case "BUSY" -> terminalState = new BusyState(terminal);
        case "SILENCE" -> terminalState = new SilenceState(terminal);
        default -> throw new UnrecognizedEntryException("Invalid terminal state");
      }

      // set terminal state
      terminal.setTerminalState(terminalState);

      // insert terminal into db
      _terminals.insert(terminal);

    } catch (Exception e) {
      throw new UnrecognizedEntryException("Error while processing client");
    }
  }

  /*
   * Process a friends entry.
   * 
   * @param parsedCommand fields of the entry
   * 
   * @throws UnrecognizedEntryException if the entry is not correct
   */
  private void processFriends(String[] parsedCommand) throws UnrecognizedEntryException {
    if (parsedCommand.length != 3) {
      throw new UnrecognizedEntryException("Invalid number of arguments");
    }

    try {
      String terminalId = parsedCommand[1];
      String friendsId = parsedCommand[2];

      String[] parsedFriendsId = friendsId.split(",");

      Terminal terminal = _terminals.findById(terminalId);

      for (String friendId : parsedFriendsId) {
        terminal.addFriend(friendId);
      }
    } catch (Exception e) {
      throw new UnrecognizedEntryException("Error while processing terminal");
    }
  }
}