package prr.terminals;

import prr.clients.Client;
import prr.communications.FinishedCommunicationState;
import prr.communications.VideoCommunication;

import java.io.Serializable;

public class FancyTerminal extends Terminal implements Serializable {
  public FancyTerminal(String id, Client client) {
    super(id, client);
  }

  public void videoCommunicationIsPossible() {
  }

  public VideoCommunication startVideoCommunication(int communication_id, Terminal receiver) {
    VideoCommunication videoCommunication = new VideoCommunication(communication_id, this, receiver);

    // set the communication as the current one
    setCurrentCommunication(videoCommunication);

    setOldState(getState());

    setTerminalState(new BusyState(this));

    registerSentCommunication(videoCommunication);

    receiver.setCurrentCommunication(videoCommunication);

    receiver.setOldState(receiver.getState());

    receiver.setTerminalState(new BusyState(receiver));

    receiver.registerReceivedCommunication(videoCommunication);

    return videoCommunication;
  }

  public double endCurrentVideoCommunication(int duration) {
    VideoCommunication currentVideoComm = (VideoCommunication) getCurrentCommunication();

    currentVideoComm.setDuration(duration);
    currentVideoComm.setCommunicationState(new FinishedCommunicationState(currentVideoComm));

    Terminal destination_terminal = currentVideoComm.getDestination();
    destination_terminal.setCurrentCommunication(null);
    destination_terminal.setTerminalState(destination_terminal.getOldState());
    destination_terminal.setOldState(null);

    setCurrentCommunication(null);
    setTerminalState(getOldState());
    setOldState(null);

    double cost = addDebt(currentVideoComm);

    currentVideoComm.setCost(cost);

    return cost;
  }

  @Override
  public String getType() {
    return "FANCY";
  }

}
