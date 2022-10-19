package prr;

import java.io.Serializable;

import prr.database.Database;

//import exceptions
import java.io.IOException;
import prr.exceptions.UnrecognizedEntryException;

import prr.terminals.FancyTerminal;
import prr.terminals.Terminal;
import prr.terminals.TerminalState;
// FIXME add more import if needed (cannot import from pt.tecnico or prr.app)

/**
 * Class Store implements a store.
 */
public class Network implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202208091753L;

  private Database _database = newDataBase();

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
      BufferedReader s = new BufferedReader(new FileReader(textFile));
      String line;
      while ((line = s.readLine()) != null) {
        String[] parsedCommand = line.split("\\|");

        String commandType = parsedCommand[0];
        switch (commandType) {
          case "CLIENT" -> processClient(parsedCommand);
          case "BASIC" -> processBasicTerminal(parsedCommand);
          case "FANCY" -> processFancyTerminal(parsedCommand);
          case "FRIENDS" -> processFriends(parsedCommand);
          default -> throw new UnrecognizedEntryException(String.join("|", fields));
        }
      }
    } catch (UnrecognizedEntryException e) {
      throw new UnrecognizedEntryException("File not found");
    }
  }

  /*
   * Process a client entry.
   * 
   * @param fields fields of the entry
   * 
   * @throws UnrecognizedEntryException if the entry is not correct
   */
  private void processClient(String[] parsedCommand) throws UnrecognizedEntryException {
    if (parsedCommand.length != 3) {
      throw new UnrecognizedEntryException("Invalid number of arguments");
    }

    try {
      String id = parsedCommand[1];
      String name = parsedCommand[2];
      String nif = parsedCommand[3];

      // TODO: ?? call a checker method on the id, name and nif
      // TODO: throw exception in case of invalid parameters
      Client client = new Client(id, name, nif);
      _database._clients.insert(client);

    } catch (IOException e) {
      throw new UnrecognizedEntryException("Error while processing client");
    }
  }

  private void processTerminal(String[] parsedCommand) throws UnrecognizedEntryException {
    if (parsedCommand.length != 3) {
      throw new UnrecognizedEntryException("Invalid number of arguments");
    }

    try {
      String terminal_type = parsedCommand[0];

      String id = parsedCommand[1];
      String clientId = parsedCommand[2];
      String terminalStateName = parsedCommand[3];

      Client client = _database._clients.findById(clientId);
      // TODO: ?? call a checker method on the id, name and nif
      // TODO: throw exception in case of invalid parameters
      if (terminal_type == "BASIC") {
        Terminal terminal = new Terminal(id, client, state);
      } else {
        Terminal terminal = new FancyTerminal(id, client, state);
      }

      _database._terminals.register(terminal);
    } catch (IOException e) {
      throw new UnrecognizedEntryException("Error while processing client");
    }
  }

}