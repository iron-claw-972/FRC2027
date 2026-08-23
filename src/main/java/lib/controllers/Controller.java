package lib.controllers;

import org.wpilib.driverstation.Joystick;
import org.wpilib.command2.button.Trigger;

import java.util.function.BooleanSupplier;

public class Controller {
  protected final Joystick controller;

  public Controller(int port) {
    this.controller = new Joystick(port);
  }

  public Trigger get(BooleanSupplier sup) {
    return new Trigger(sup);
  }
}
