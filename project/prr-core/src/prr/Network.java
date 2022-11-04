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
import prr.notifications.CustomNotification;
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

//TODO: Change show... to get... etc etc...

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

  public Collection<Client> getAllClients() {
    return _clients.values();
  }

  public void disableClientNotifications(String client_id)
      throws UnknownClientKeyException, ClientNotificationsAlreadyDisabledException {
    Client client = getClient(client_id);

    if (client == null) {
      throw new UnknownClientKeyException(client_id);
    }

    if (!(client.getNotificationsStatus())) {
      throw new ClientNotificationsAlreadyDisabledException();
    }

    client.toggleNotifications();
  }

  public void enableClientNotifications(String client_id)
      throws UnknownClientKeyException, ClientNotificationsAlreadyEnabledException {
    Client client = getClient(client_id);

    if (client == null) {
      throw new UnknownClientKeyException(client_id);
    }

    if (client.getNotificationsStatus()) {
      throw new ClientNotificationsAlreadyEnabledException();
    }

    client.toggleNotifications();
  }

  public long getClientPayments(String client_id) throws UnknownClientKeyException {
    Client client = getClient(client_id);

    if (client == null) {
      throw new UnknownClientKeyException(client_id);
    }

    return Math.round(client.getClientPayments());
  }

  public long getClientDebts(String client_id) throws UnknownClientKeyException {
    Client client = getClient(client_id);

    if (client == null) {
      throw new UnknownClientKeyException(client_id);
    }

    return Math.round(client.getClientDebts());
  }

  public ArrayList<Client> getClientsWithAndWithoutDebts(boolean withDebt) {
    // ordenar de forma descendente pelas dividas e desempate crescente pelas keys
    // do cliente

    Collection<Client> clients = _clients.values().stream()
        .filter(c -> withDebt ? c.getClientDebts() > 0 : c.getClientDebts() == 0).collect(Collectors.toList());

    ArrayList<Client> clientsArr = new ArrayList<Client>(clients);

    clientsArr.sort(Comparator.comparing(Client::getClientDebts).reversed().thenComparing(Client::getId));

    return clientsArr;
  }

  // Terminal methods
  public void registerTerminal(String terminal_id, String terminal_type, String client_id)
      throws UnknownClientKeyException, DuplicateTerminalKeyException, InvalidTerminalKeyException {

    if (terminal_id.length() != 6) {
      throw new InvalidTerminalKeyException(terminal_id);
    }

    // check if the string is parsable meaning its a number
    // TOMAS - precisaram de fazer isto ou simplesmente definel terminal_id como um
    // inteiro???? (se sim como é que depois sao feitos os catch?)
    try {
      Integer.parseInt(terminal_id);
    } catch (NumberFormatException e) {
      throw new prr.exceptions.InvalidTerminalKeyException(terminal_id);
    }

    // Check if Terminal exists in treeMap
    if (_terminals.get(terminal_id) != null) { // FIX use .get or getTerminal??
      throw new DuplicateTerminalKeyException(terminal_id);
    }

    // TOMAS - o construtor do terminal recebe um cliente ou uma string de um
    // cliente (I would say its better to be a string but I am a pleb)??
    // JOAO - como espaco e performance nao importam é mais facil ja guardar o
    // cliente
    // mas fica guardado como ponteiro ou é copia ? Depende se a variavel cliente
    // esta atualizado ou nao
    Client client = getClient(client_id);

    Terminal terminal;
    if (terminal_type.equals("BASIC")) {
      terminal = new Terminal(terminal_id, client);
    } else {
      terminal = new FancyTerminal(terminal_id, client);
    }

    // TOMAS - comom é que fizeram o terminal state?
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
    terminalFriend.removeFriend(terminal);
    terminal.removeFriend(terminalFriend);
  }

  public void turnOnTerminal(Terminal terminal) throws TerminalAlreadyOnException {
    if (terminal.getTerminalState().toString().equals("IDLE"))
      throw new TerminalAlreadyOnException();

    // TODO: check if setTerminalState or setIdleState (need to create this method)
    terminal.setTerminalState(new IdleState(terminal));
  }

  public void turnOffTerminal(Terminal terminal) throws TerminalAlreadyOffException {
    if (terminal.getTerminalState().toString().equals("OFF"))
      throw new TerminalAlreadyOffException();

    // TODO: check if setTerminalState or setIdleState (need to create this method)
    terminal.setTerminalState(new OffState(terminal));
  }

  public void silenceTerminal(Terminal terminal) throws TerminalAlreadySilenceException {
    if (terminal.getTerminalState().toString().equals("SILENCE"))
      throw new TerminalAlreadySilenceException();

    // TODO: check if setTerminalState or setIdleState (need to create this method)
    terminal.setTerminalState(new SilenceState(terminal));
  }

  public void performPayment(Terminal terminal, int communication_id)
      throws BadPaymentException, UnknownCommunicationKeyException {

    Communication terminalCurrentCommunication = terminal.getCurrentCommunication();
    Communication communication = getCommunication(communication_id);

    if (!communication.getSender().getId().equals(terminal.getId()) || !communication.isFinished()) {
      throw new BadPaymentException();
    }

    terminal.pay(communication);
  }

  // TODO: check if client have enabled notifications

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

  // TODO: create exception
  // TODO: check if communication is null
  public Communication getCurrentCommunication(Terminal terminal) throws NoCommunicationException {
    Communication currentCommunication = terminal.getCurrentCommunication();

    if (currentCommunication == null)
      throw new NoCommunicationException();

    return currentCommunication;
  }

  public void startInteractiveCommunication(Terminal sender, String receiver_id, String communication_type)
      throws UnsupportedAtOriginException, UnsupportedAtDestinationException, DestinationIsOffException,
      DestinationIsBusyException, DestinationIsSilentException, UnknownTerminalKeyException {
    switch (communication_type) {
      case "VOICE" -> startVoiceCommunication(sender, receiver_id);
      case "VIDEO" -> startVideoCommunication(sender, receiver_id);
    }
  }

  public long endInteraciveCommunication(Terminal sender, int duration) {
    double cost = 0;
    switch (sender.getCurrentCommunication().getType()) {
      case "VOICE" -> cost = endVoiceCommunication(sender, duration);
      case "VIDEO" -> cost = endVideoCommunication(sender, duration);
    }
    return Math.round(cost);
  }

  public TextCommunication sendTextCommunication(Terminal sender, String receiver_id, String message)
      throws DestinationIsOffException, UnknownTerminalKeyException {

    Terminal receiver = getTerminal(receiver_id);

    if (receiver.getTerminalState().toString().equals("OFF"))
      throw new DestinationIsOffException();

    int communication_id = ++_communication_id_counter;

    TextCommunication textComm = sender.sendTextCommunication(communication_id, receiver, message);

    _communications.put(textComm.getId(), textComm);

    return textComm;
  }

  public VoiceCommunication startVoiceCommunication(Terminal sender, String receiver_id)
      throws DestinationIsOffException, DestinationIsBusyException, DestinationIsSilentException,
      UnknownTerminalKeyException {

    Terminal receiver = getTerminal(receiver_id);

    if (receiver.getTerminalState().toString().equals("OFF"))
      throw new DestinationIsOffException();

    if (receiver.getTerminalState().toString().equals("BUSY") || sender.getId().equals(receiver_id))
      throw new DestinationIsBusyException();

    if (receiver.getTerminalState().toString().equals("SILENCE"))
      throw new DestinationIsSilentException();

    int communication_id = ++_communication_id_counter;

    VoiceCommunication voiceComm = sender.startVoiceCommunication(communication_id, receiver);

    _communications.put(voiceComm.getId(), voiceComm);

    return voiceComm;
  }

  public double endVoiceCommunication(Terminal sender, int duration) {
    return sender.endCurrentVoiceCommunication(duration);
  }

  public VideoCommunication startVideoCommunication(Terminal sender, String receiver_id)
      throws UnsupportedAtOriginException, UnsupportedAtDestinationException, DestinationIsOffException,
      DestinationIsBusyException, DestinationIsSilentException, UnknownTerminalKeyException {

    Terminal receiver = getTerminal(receiver_id);

    if (sender.getTerminalType().equals("BASIC"))
      throw new UnsupportedAtOriginException();

    if (receiver.getTerminalType().equals("BASIC"))
      throw new UnsupportedAtDestinationException();

    if (receiver.getTerminalState().toString().equals("OFF")) {
      throw new DestinationIsOffException();
    }

    if (receiver.getTerminalState().toString().equals("BUSY") || sender.getId().equals(receiver_id))
      throw new DestinationIsBusyException();

    if (receiver.getTerminalState().toString().equals("SILENCE"))
      throw new DestinationIsSilentException();

    int communication_id = ++_communication_id_counter;

    FancyTerminal fancySender = (FancyTerminal) sender;

    VideoCommunication videoComm = fancySender.startVideoCommunication(communication_id, (FancyTerminal) receiver);

    _communications.put(videoComm.getId(), videoComm);

    return videoComm;
  }

  public double endVideoCommunication(Terminal sender, int duration) {
    FancyTerminal fancySender = (FancyTerminal) sender;

    return fancySender.endCurrentVideoCommunication(duration);
  }

  public long getGlobalPayments() {
    long total = 0;
    for (Client client : getAllClients()) {
      total += client.getClientPayments();
    }

    return total;
  }

  public long getGlobalDebts() {
    long total = 0;
    for (Client client : getAllClients()) {
      total += client.getClientDebts();
    }

    return total;
  }

  // TODO: a verificação se já foi pago fica no network

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