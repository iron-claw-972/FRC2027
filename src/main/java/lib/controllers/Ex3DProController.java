package lib.controllers;

import org.wpilib.driverstation.Joystick;
import org.wpilib.driverstation.POVDirection;
import org.wpilib.command2.button.Trigger;

public class Ex3DProController extends Controller {
  public Ex3DProController(int port) {
    super(port);
  }

  public enum Ex3DProButton {
    B1(1),
    B2(2),
    B3(3),
    B4(4),
    B6(6),
    B7(7),
    B8(8),
    B9(9),
    B10(10),
    B11(11),
    B12(12);

    public final int id;

    Ex3DProButton(final int id) {
      this.id = id;
    }
  }

  public enum Ex3DProAxis {
    X(0),
    Y(1),
    Z(2),
    SLIDER(3);

    public final int id;

    Ex3DProAxis(final int id) {
      this.id = id;
    }
  }

  public enum Ex3DProHatSwitch {
    UNPRESSED(POVDirection.CENTER),
    UP(POVDirection.UP),
    UP_RIGHT(POVDirection.UP_RIGHT),
    RIGHT(POVDirection.RIGHT),
    DOWN_RIGHT(POVDirection.DOWN_RIGHT),
    DOWN(POVDirection.DOWN),
    DOWN_LEFT(POVDirection.DOWN_LEFT),
    LEFT(POVDirection.LEFT),
    UP_LEFT(POVDirection.UP_LEFT);

    public final POVDirection angle;

    Ex3DProHatSwitch(final POVDirection angle) {
      this.angle = angle;
    }
  }

  public Trigger get(Ex3DProButton button) {
    return new Trigger(() -> controller.getRawButton(button.id));
  }

  public double get(Ex3DProAxis axis) {
    return controller.getRawAxis(axis.id);
  }

  public Trigger get(Ex3DProHatSwitch hatSwitch) {
    return new Trigger(() -> controller.getPOV() == hatSwitch.angle);
  }

  public Joystick get() {
    return controller;
  }
}
