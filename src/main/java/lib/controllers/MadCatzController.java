package lib.controllers;

import org.wpilib.driverstation.Joystick;
import org.wpilib.driverstation.POVDirection;
import org.wpilib.command2.button.Trigger;

public class MadCatzController extends Controller {
  public final Trigger
      ALL_UP =
          get(MadCatzHatSwitch.UP)
              .or(get(MadCatzHatSwitch.UP_LEFT))
              .or(get(MadCatzHatSwitch.UP_RIGHT)),
      ALL_DOWN =
          get(MadCatzHatSwitch.DOWN)
              .or(get(MadCatzHatSwitch.DOWN_LEFT))
              .or(get(MadCatzHatSwitch.DOWN_RIGHT)),
      ALL_LEFT =
          get(MadCatzHatSwitch.LEFT)
              .or(get(MadCatzHatSwitch.UP_LEFT))
              .or(get(MadCatzHatSwitch.DOWN_LEFT)),
      ALL_RIGHT =
          get(MadCatzHatSwitch.RIGHT)
              .or(get(MadCatzHatSwitch.UP_RIGHT))
              .or(get(MadCatzHatSwitch.DOWN_RIGHT));

  public MadCatzController(int port) {
    super(port);
  }

  public enum MadCatzButton {
    B1(1),
    B2(2),
    B3(3),
    B4(4),
    B6(6),
    B7(7);

    public final int id;

    MadCatzButton(final int id) {
      this.id = id;
    }
  }

  public enum MadCatzAxis {
    X(0),
    Y(1),
    SLIDER(2),
    ZROTATE(3);

    public final int id;

    MadCatzAxis(final int id) {
      this.id = id;
    }
  }

  public enum MadCatzHatSwitch {
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

    MadCatzHatSwitch(final POVDirection angle) {
      this.angle = angle;
    }
  }

  public Trigger get(MadCatzButton button) {
    return new Trigger(() -> controller.getRawButton(button.id));
  }

  public double get(MadCatzAxis axis) {
    return controller.getRawAxis(axis.id);
  }

  public Trigger get(MadCatzHatSwitch hatSwitch) {
    return new Trigger(() -> controller.getPOV() == hatSwitch.angle);
  }

  public Joystick get() {
    return controller;
  }
}
