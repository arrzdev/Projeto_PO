package prr;

import java.io.Serializable;
import java.io.BufferedReader;
import java.io.FileReader;

//structures
import java.util.Collection;
import java.util.function.Predicate;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.stream.Collectors;

//import exceptions
import java.io.IOException;

import prr.exceptions.UnknownClientKeyException;
import prr.exceptions.UnknownTerminalKeyException;
import prr.exceptions.UnrecognizedEntryException;
import prr.exceptions.DuplicateClientKeyException;

import prr.exceptions.DuplicateTerminalKeyException;
import prr.exceptions.InvalidTerminalKeyException;

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

  private TreeMap<String, Client> _clients = new TreeMap<String, Client>(String.CASE_INSENSITIVE_ORDER);
  private TreeMap<String, Terminal> _terminals = new TreeMap<String, Terminal>(String.CASE_INSENSITIVE_ORDER);

  // Client methods
  public void registerClient(String id, String name, int nif) throws DuplicateClientKeyException {
    if (_clients.get(id) != null) { // FIX use .get or getClient??
      throw new DuplicateClientKeyException(id);
    }

    Client client = new Client(id, name, nif);
    _clients.put(id, client);
  }

  public Client getClient(String id) throws UnknownClientKeyException {
    Client client = _clients.get(id);

    if (client == null) {
      throw new UnknownClientKeyException(id);
    }

    return client;
  }

  public Collection<Client> getClients() {
    return _clients.values();
  }

  // Terminal methods

  public void registerTerminal(String terminalId, String terminalType, String clientId)
      throws UnknownClientKeyException, DuplicateTerminalKeyException, InvalidTerminalKeyException {

    if (terminalId.length() != 6) {
      throw new InvalidTerminalKeyException(terminalId);
    }

    // check if the string is parsable meaning its a number
    // TOMAS - precisaram de fazer isto ou simplesmente definel terminalId como um
    // inteiro???? (se sim como é que depois sao feitos os catch?)
    try {
      Integer.parseInt(terminalId);
    } catch (NumberFormatException e) {
      throw new prr.exceptions.InvalidTerminalKeyException(terminalId);
    }

    // Check if Terminal exists in treeMap
    if (_terminals.get(terminalId) != null) { // FIX use .get or getTerminal??
      throw new DuplicateTerminalKeyException(terminalId);
    }

    // TOMAS - o construtor do terminal recebe um cliente ou uma string de um
    // cliente (I would say its better to be a string but I am a pleb)??
    Client client = getClient(clientId);

    Terminal terminal;
    if (terminalType.equals("BASIC")) {
      terminal = new Terminal(terminalId, client);
    } else {
      terminal = new FancyTerminal(terminalId, client);
    }

    // TOMAS - comom é que fizeram o terminal state?
    // insert the terminal into the treeMap
    _terminals.put(terminalId, terminal);

    // insert the terminal into the client
    client.addTerminal(terminal);
  }

  public Terminal getTerminal(String terminalId) throws UnknownTerminalKeyException {
    Terminal terminal = _terminals.get(terminalId);

    if (terminal == null) {
      throw new UnknownTerminalKeyException(terminalId);
    }

    return terminal;
  }

  public Collection<Terminal> getTerminals() {
    return _terminals.values();
  }

  public Collection<Terminal> getUnusedTerminals() {
    Predicate<Terminal> filterPredicate = terminal -> terminal.getTotalCommunicationsCount() == 0;

    return _terminals.values().stream().filter(filterPredicate).collect(Collectors.toList());
  }

  /*
   * Read text input file and create corresponding domain entities.
   * 
   * @param filename name of the text input file
   * 
   * @throws UnrecognizedEntryException if some entry is not correct
   * 
   * @throws IOException if there is an IO erro while processing
   * the text file
   */
  void importFile(String filename) throws UnrecognizedEntryException, IOException {
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
      int nif = Integer.parseInt(parsedCommand[3]);

      registerClient(id, name, nif);

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
      String terminalType = parsedCommand[0];

      String terminalId = parsedCommand[1];
      String clientId = parsedCommand[2];
      String terminalStateName = parsedCommand[3];

      // register the terminal into the treeMap
      registerTerminal(terminalId, terminalType, clientId);

      // get it and change its state
      Terminal terminal = getTerminal(terminalId);

      switch (terminalStateName) {
        case "ON" -> terminal.setTerminalState(new IdleState(terminal));
        case "OFF" -> terminal.setTerminalState(new OffState(terminal));
        case "BUSY" -> terminal.setTerminalState(new BusyState(terminal));
        case "SILENCE" -> terminal.setTerminalState(new SilenceState(terminal));
        default -> throw new UnrecognizedEntryException("Invalid terminal state");
      }

    } catch (Exception e) { // Catch ?????
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

      Terminal terminal = getTerminal(terminalId);

      for (String friendId : parsedFriendsId) {
        terminal.addFriend(friendId);
      }
    } catch (Exception e) {
      throw new UnrecognizedEntryException("Error while processing terminal");
    }
  }
}