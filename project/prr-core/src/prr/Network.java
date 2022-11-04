package prr;

import java.io.Serializable;
import java.io.BufferedReader;
import java.io.FileReader;

//structures
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import java.lang.Math;

//import exceptions
import java.io.IOException;

import prr.exceptions.UnknownClientKeyException;
import prr.exceptions.UnknownTerminalKeyException;
import prr.exceptions.UnrecognizedEntryException;
import prr.exceptions.DuplicateClientKeyException;

import prr.exceptions.DuplicateTerminalKeyException;
import prr.exceptions.InvalidTerminalKeyException;
import prr.exceptions.NoCommunicationException;
import prr.exceptions.TerminalAlreadyOffException;
import prr.exceptions.TerminalAlreadyOnException;
import prr.exceptions.TerminalAlreadySilenceException;

import prr.exceptions.UnknownCommunicationKeyException;

import prr.exceptions.BadPaymentException;

import prr.exceptions.ClientNotificationsAlreadyEnabledException;
import prr.exceptions.ClientNotificationsAlreadyDisabledException;

import prr.exceptions.DestinationIsOffException;
import prr.exceptions.DestinationIsBusyException;
import prr.exceptions.DestinationIsSilentException;

import prr.exceptions.UnsupportedAtDestinationException;
import prr.exceptions.UnsupportedAtOriginException;
import prr.notifications.ClientNotification;
import prr.exceptions.InvalidCommunicationException;

//clients
import prr.clients.Client;
import prr.communications.Communication;
import prr.communications.TextCommunication;
import prr.communications.VoiceCommunication;
import prr.communications.VideoCommunication;

//terminals
import prr.terminals.Terminal;
import prr.terminals.BusyState;

import prr.terminals.TerminalState;
import prr.terminals.IdleState;
import prr.terminals.SilenceState;
import prr.terminals.FancyTerminal;
import prr.terminals.OffState;

//communications
import prr.communications.TextCommunication;

/**
 * Class Store implements a store.
 */
public class Network implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202208091753L;

  private TreeMap<String, Client> _clients = new TreeMap<String, Client>(String.CASE_INSENSITIVE_ORDER);
  private TreeMap<String, Terminal> _terminals = new TreeMap<String, Terminal>(String.CASE_INSENSITIVE_ORDER);
  private TreeMap<Integer, Communication> _communications = new TreeMap<Integer, Communication>();

  private int _communication_id_counter = 0;

  // Client methods
  public void registerClient(String id, String name, int nif) throws DuplicateClientKeyException {
    if (_clients.get(id) != null) {
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

  public Collection<Client> getAllClients() {
    return _clients.values();
  }

  public void disableClientNotifications(String client_id)
      throws UnknownClientKeyException, ClientNotificationsAlreadyDisabledException {
    Client client = getClient(client_id);

    if (client == null) {
      throw new UnknownClientKeyException(client_id);
    }

    if (!(client.canReceiveNotifications())) {
      throw new ClientNotificationsAlreadyDisabledException();
    }

    client.toggleReceiveNotifications();
  }

  public void enableClientNotifications(String client_id)
      throws UnknownClientKeyException, ClientNotificationsAlreadyEnabledException {
    Client client = getClient(client_id);

    if (client == null) {
      throw new UnknownClientKeyException(client_id);
    }

    if (client.canReceiveNotifications()) {
      throw new ClientNotificationsAlreadyEnabledException();
    }

    client.toggleReceiveNotifications();
  }

  public long getClientPayments(String client_id) throws UnknownClientKeyException {
    Client client = getClient(client_id);

    if (client == null) {
      throw new UnknownClientKeyException(client_id);
    }

    return Math.round(client.getPayments());
  }

  public long getClientDebts(String client_id) throws UnknownClientKeyException {
    Client client = getClient(client_id);

    if (client == null) {
      throw new UnknownClientKeyException(client_id);
    }

    return Math.round(client.getDebts());
  }

  public ArrayList<Client> getClientsWithAndWithoutDebts(boolean withDebt) {
    Collection<Client> clients = _clients.values().stream()
        .filter(client -> withDebt ? client.getDebts() > 0 : client.getDebts() == 0).collect(Collectors.toList());

    ArrayList<Client> clientsArr = new ArrayList<Client>(clients);

    clientsArr.sort(Comparator.comparing(Client::getDebts).reversed().thenComparing(Client::getId));
    return clientsArr;
  }

  public ArrayList<String> getClientNotifications(String client_id) throws UnknownClientKeyException {
    Client client = getClient(client_id);

    if (client == null) {
      throw new UnknownClientKeyException(client_id);
    }

    ArrayList<String> notifications = new ArrayList<String>();
    for (ClientNotification cn : client.getNotifications()) {
      notifications.add(cn.toString());
    }

    return notifications;
  }

  // Terminal methods
  public void registerTerminal(String terminal_id, String terminal_type, String client_id)
      throws UnknownClientKeyException, DuplicateTerminalKeyException, InvalidTerminalKeyException {

    if (terminal_id.length() != 6) {
      throw new InvalidTerminalKeyException(terminal_id);
    }

    try {
      Integer.parseInt(terminal_id);
    } catch (NumberFormatException e) {
      throw new prr.exceptions.InvalidTerminalKeyException(terminal_id);
    }

    // Check if Terminal exists in treeMap
    if (_terminals.get(terminal_id) != null) {
      throw new DuplicateTerminalKeyException(terminal_id);
    }

    Client client = getClient(client_id);

    Terminal terminal;
    if (terminal_type.equals("BASIC")) {
      terminal = new Terminal(terminal_id, client);
    } else {
      terminal = new FancyTerminal(terminal_id, client);
    }

    // insert the terminal into the treeMap
    _terminals.put(terminal_id, terminal);

    // insert the terminal into the client
    client.addTerminal(terminal);
  }

  public Terminal getTerminal(String terminal_id) throws UnknownTerminalKeyException {
    Terminal terminal = _terminals.get(terminal_id);

    if (terminal == null) {
      throw new UnknownTerminalKeyException(terminal_id);
    }

    return terminal;
  }

  public Collection<Terminal> getAllTerminals() {
    return _terminals.values();
  }

  public Collection<Terminal> getUnusedTerminals() {
    Predicate<Terminal> filterPredicate = terminal -> terminal.getTotalCommunicationsCount() == 0;

    return _terminals.values().stream().filter(filterPredicate).collect(Collectors.toList());
  }

  public Collection<Terminal> getTerminalsWithPositiveBalance() {
    Predicate<Terminal> filterPredicate = terminal -> terminal.getPayments() - terminal.getDebts() > 0;

    return _terminals.values().stream().filter(filterPredicate).collect(Collectors.toList());
  }

  public long getTerminalPayments(Terminal terminal) {
    return Math.round(terminal.getPayments());
  }

  public long getTerminalDebts(Terminal terminal) {
    return Math.round(terminal.getDebts());
  }

  // Consult Terminal methods

  public void addFriend(Terminal terminal, String friend_id) throws UnknownTerminalKeyException {
    // check if they are already friends or if we are trying to add ourselves
    if (terminal.isFriend(friend_id) || terminal.getId().equals(friend_id))
      return;

    Terminal terminalFriend = getTerminal(friend_id);

    // add friend to each other
    terminal.addFriend(terminalFriend);
  }

  public void removeFriend(Terminal terminal, String friend_id) throws UnknownTerminalKeyException {
    if (!terminal.isFriend(friend_id) || terminal.getId().equals(friend_id))
      return;

    Terminal terminalFriend = getTerminal(friend_id);

    // add friend to each other
    terminal.removeFriend(terminalFriend);
  }

  // Modify Terminal States

  public void turnOnTerminal(Terminal terminal) throws TerminalAlreadyOnException {
    terminal.getState().turnOn();
  }

  public void turnOffTerminal(Terminal terminal) throws TerminalAlreadyOffException {
    terminal.getState().turnOff();
  }

  public void silenceTerminal(Terminal terminal) throws TerminalAlreadySilenceException {
    terminal.getState().changeToSilence();
  }

  public void performPayment(Terminal terminal, int communication_id)
      throws BadPaymentException, UnknownCommunicationKeyException {

    Communication communication = getCommunication(communication_id);
    if (!communication.getOriginTerminal().getId().equals(terminal.getId()) || !communication.isFinished()
        || communication.isPaid()) {
      throw new BadPaymentException();
    }

    terminal.pay(communication);
  }

  // Communication methods
  public Collection<Communication> getAllCommunications() {
    return _communications.values();
  }

  public ArrayList<Communication> getCommunicationsFromClient(String client_id) throws UnknownClientKeyException {
    Client client = _clients.get(client_id);

    if (client == null) {
      throw new UnknownClientKeyException(client_id);
    }

    return client.getSentCommunications();
  }

  public ArrayList<Communication> getCommunicationsToClient(String client_id) throws UnknownClientKeyException {
    Client c = _clients.get(client_id);

    if (c == null) {
      throw new UnknownClientKeyException(client_id);
    }

    return c.getReceivedCommunications();
  }

  public Communication getCommunication(int communication_id) throws UnknownCommunicationKeyException {
    Communication communication = _communications.get(communication_id);

    if (communication == null)
      throw new UnknownCommunicationKeyException();

    return communication;
  }

  public Communication getCurrentCommunication(Terminal terminal) throws NoCommunicationException {
    Communication currentCommunication = terminal.getCurrentCommunication();

    if (currentCommunication == null)
      throw new NoCommunicationException();

    return currentCommunication;
  }

  public void startInteractiveCommunication(Terminal origin_terminal, String destination_id, String communication_type)
      throws UnsupportedAtOriginException, UnsupportedAtDestinationException, DestinationIsOffException,
      DestinationIsBusyException, DestinationIsSilentException, UnknownTerminalKeyException {

    try {
      switch (communication_type) {
        case "VOICE" -> startVoiceCommunication(origin_terminal, destination_id);
        case "VIDEO" -> startVideoCommunication(origin_terminal, destination_id);
      }
    } catch (DestinationIsOffException | DestinationIsBusyException | DestinationIsSilentException e) {
      getTerminal(destination_id).addClientToNotify(origin_terminal.getClient());
      throw e;
    }
  }

  public long endInteraciveCommunication(Terminal origin_terminal, int duration) {
    double cost = 0;
    switch (origin_terminal.getCurrentCommunication().getType()) {
      case "VOICE" -> cost = endVoiceCommunication(origin_terminal, duration);
      case "VIDEO" -> cost = endVideoCommunication(origin_terminal, duration);
    }
    return Math.round(cost);
  }

  // Text Communication methods
  public TextCommunication sendTextCommunication(Terminal origin_terminal, String destination_id, String message)
      throws DestinationIsOffException, UnknownTerminalKeyException {

    Terminal destination_terminal = getTerminal(destination_id);

    // handle the raise of the exceptions when the terminal state is not compatible
    // with the communication
    try {
      destination_terminal.getState().textCommunicationIsPossible();
    } catch (DestinationIsOffException e) {
      destination_terminal.addClientToNotify(origin_terminal.getClient());
      throw e;
    }

    int communication_id = ++_communication_id_counter;
    TextCommunication textCommunication = origin_terminal.sendTextCommunication(communication_id, destination_terminal,
        message);
    _communications.put(textCommunication.getId(), textCommunication);

    return textCommunication;
  }

  // Voice Communication methods
  public VoiceCommunication startVoiceCommunication(Terminal origin_terminal, String destination_id)
      throws DestinationIsOffException, DestinationIsBusyException, DestinationIsSilentException,
      UnknownTerminalKeyException {

    // if we are string to start a voice communication with ourselves
    if (destination_id.equals(origin_terminal.getId()))
      throw new DestinationIsBusyException();

    Terminal destination_terminal = getTerminal(destination_id);

    // handle the raise of the exceptions when the terminal state is not compatible
    // with the communication
    destination_terminal.getState().voiceCommunicationIsPossible();

    // get the new communication counter
    int communication_id = ++_communication_id_counter;

    // create the new communication
    VoiceCommunication voiceComm = origin_terminal.startVoiceCommunication(communication_id, destination_terminal);

    // register communication on network
    _communications.put(voiceComm.getId(), voiceComm);

    return voiceComm;
  }

  public double endVoiceCommunication(Terminal origin_terminal, int duration) {
    return origin_terminal.endCurrentVoiceCommunication(duration);
  }

  // Video Communication methods
  public VideoCommunication startVideoCommunication(Terminal origin_terminal, String destination_id)
      throws UnsupportedAtOriginException, UnsupportedAtDestinationException, DestinationIsOffException,
      DestinationIsBusyException, DestinationIsSilentException, UnknownTerminalKeyException {

    Terminal destination_terminal = getTerminal(destination_id);

    if (destination_id.equals(origin_terminal.getId())) {
      throw new DestinationIsBusyException();
    }

    // handle the raise of the exceptions when the communication is not compatible
    // with the terminals
    origin_terminal.videoCommunicationIsPossible();
    destination_terminal.videoCommunicationIsPossible();

    // handle the raise of the exceptions when the terminal state is not compatible
    // with the communication
    destination_terminal.getState().videoCommunicationIsPossible();

    int communication_id = ++_communication_id_counter;
    FancyTerminal fancy_origin_terminal = (FancyTerminal) origin_terminal;
    VideoCommunication videoCommunication = fancy_origin_terminal.startVideoCommunication(communication_id,
        (FancyTerminal) destination_terminal);

    _communications.put(videoCommunication.getId(), videoCommunication);
    return videoCommunication;
  }

  public double endVideoCommunication(Terminal origin_terminal, int duration) {
    FancyTerminal fancy_origin_terminal = (FancyTerminal) origin_terminal;

    return fancy_origin_terminal.endCurrentVideoCommunication(duration);
  }

  // Global lookup methods
  public long getGlobalPayments() {
    long total = 0;
    for (Client client : getAllClients()) {
      total += client.getPayments();
    }

    return total;
  }

  public long getGlobalDebts() {
    long total = 0;
    for (Client client : getAllClients()) {
      total += client.getDebts();
    }

    return total;
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
      String terminal_type = parsedCommand[0];

      String terminal_id = parsedCommand[1];
      String client_id = parsedCommand[2];
      String terminalStateName = parsedCommand[3];

      // register the terminal into the treeMap
      registerTerminal(terminal_id, terminal_type, client_id);

      // get it and change its state
      Terminal terminal = getTerminal(terminal_id);

      TerminalState terminalState;
      switch (terminalStateName) {
        case "ON" -> terminalState = new IdleState(terminal);
        case "OFF" -> terminalState = new OffState(terminal);
        case "BUSY" -> terminalState = new BusyState(terminal);
        case "SILENCE" -> terminalState = new SilenceState(terminal);
        default -> throw new UnrecognizedEntryException("Invalid terminal state");
      }

      terminal.setTerminalState(terminalState);

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
      String terminal_id = parsedCommand[1];
      String friendsId = parsedCommand[2];

      String[] parsedFriendsId = friendsId.split(",");

      Terminal terminal = getTerminal(terminal_id);

      for (String friendId : parsedFriendsId) {
        Terminal terminalFriend = getTerminal(friendId);
        terminal.addFriend(terminalFriend);
      }
    } catch (Exception e) {
      throw new UnrecognizedEntryException("Error while processing terminal");
    }
  }

}