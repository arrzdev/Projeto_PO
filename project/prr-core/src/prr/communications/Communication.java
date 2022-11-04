package prr.communications;

import java.io.Serializable;

import prr.terminals.Terminal;
import prr.payments.PaymentPlan;

import java.lang.Math;

public abstract class Communication implements Serializable {
  private int _id;

  private Terminal _origin;
  private Terminal _destination;
  private CommunicationState _state;

  private boolean _paid = false;

  private boolean _ended = false;
  private double _cost = 0;

  public Communication(int id, Terminal origin_terminal, Terminal receiver) {
    _id = id;
    _origin = origin_terminal;
    _destination = receiver;
    _state = new OngoingCommunicationState(this);
  }

  public void setCommunicationState(CommunicationState state) {
    _state = state;
  }

  public Terminal getOriginTerminal() {
    return _origin;
  }

  public Terminal getDestination() {
    return _destination;
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
    return String.format("%s|%d|%s|%s|%d|%d|%s", getType(), _id, _origin.getId(), _destination.getId(), getUnits(),
        Math.round(_cost),
        _state);
  }
}