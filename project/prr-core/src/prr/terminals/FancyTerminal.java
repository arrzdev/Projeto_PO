package prr.terminals;

import prr.clients.Client;

import prr.communications.VideoCommunication;

import java.io.Serializable;

public class FancyTerminal extends Terminal implements Serializable {
  public FancyTerminal(String id, Client client) {
    super(id, client);
  }

  public VideoCommunication startVideoCommunication(int communication_id, Terminal receiver) {
    VideoCommunication videoCom = new VideoCommunication(communication_id, this, receiver);

    setCurrentCommunication(videoCom);

    setTerminalState(new BusyState(this));

    registerSentCoommunication(videoCom);

    receiver.setCurrentCommunication(videoCom);

    receiver.setTerminalState(new BusyState(receiver));

    receiver.registerReceivedCoommunication(videoCom);

    return videoCom;
  }

  public double endCurrentVideoCommunication(int duration) {
    VideoCommunication currentVideoComm = (VideoCommunication) getCurrentCommunication();

    currentVideoComm.setDuration(duration);

    Terminal receiver = currentVideoComm.getReceiver();
    receiver.setCurrentCommunication(null);
    receiver.setTerminalState(new IdleState(receiver));

    setCurrentCommunication(null);
    setTerminalState(new IdleState(this));

    double cost = addDebt(currentVideoComm);

    currentVideoComm.setCost(cost);

    return cost;

  }

  @Override
  public String getTerminalType() {
    return "FANCY";
  }

}
