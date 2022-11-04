package prr.communications;

import java.io.Serializable;

import prr.terminals.Terminal;
import prr.payments.PaymentPlan;

import java.lang.Math;

public abstract class Communication implements Serializable {
  private int _id;

  private Terminal _sender;
  private Terminal _receiver;
  private CommunicationState _state;

  private boolean _paid = false;

  private boolean _ended = false;
  private double _cost = 0;

  public Communication(int id, Terminal sender, Terminal receiver) {
    _id = id;
    _sender = sender;
    _receiver = receiver;
    _state = new OngoingCommunicationState(this);
  }

  public void setCommunicationState(CommunicationState state) {
    _state = state;
  }

  public Terminal getSender() {
    return _sender;
  }

  public Terminal getReceiver() {
    return _receiver;
  }

  public boolean isOnGoing() {
    return _state.isOnGoing();
  }

  public boolean isFinished() {
    return _state.isFinished();
  }

  public int getId() {
    return _id;
  }

  public boolean isPaid() {
    return _paid;
  }

  public void setPaid() {
    _paid = true;
  }

  public void setEnded() {
    _ended = true;
  }

  public void setCost(double cost) {
    _cost = cost;
  }

  public double getCost() {
    return _cost;
  }

  abstract public String getType();

  abstract public int getUnits();

  public String toString() {
    return String.format("%s|%d|%s|%s|%d|%d|%s", getType(), _id, _sender.getId(), _receiver.getId(), getUnits(),
        Math.round(_cost),
        _state);
  }
}