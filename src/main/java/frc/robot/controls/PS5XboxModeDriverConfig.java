package frc.robot.controls;

import java.util.function.BooleanSupplier;

import org.wpilib.math.geometry.Rotation2d;
import org.wpilib.driverstation.Alliance;
import org.wpilib.command2.Command;
import org.wpilib.command2.CommandScheduler;
import org.wpilib.command2.FunctionalCommand;
import org.wpilib.command2.InstantCommand;
import org.wpilib.command2.SequentialCommandGroup;
import org.wpilib.command2.WaitCommand;
import frc.robot.Robot;
import frc.robot.constants.Constants;
import frc.robot.subsystems.drivetrain.Drivetrain;
import lib.controllers.GameController;
import lib.controllers.GameController.Axis;
import lib.controllers.GameController.Button;
import lib.controllers.GameController.DPad;

/**
 * Driver config for PS5 controllers using Xbox 360 emulation mode. This lets SCUF and other PS5
 * controllers work with WPILib rumble.
 *
 * <p>Setup: - download DSX (https://dualsensex.com/download/) - install ViGEmBus driver (if app
 * doesn't auto prompt) - in dsx, set "controller emulation" to Xbox 360 - ensure rumble is enabled
 * in dsx settings - once code is depoloyed, change controller to "Xbox 360" in driverstation
 */
public class PS5XboxModeDriverConfig extends BaseDriverConfig {
  private final GameController controller = new GameController(Constants.DRIVER_JOY);
  private final BooleanSupplier slowModeSupplier = () -> false;

  @SuppressWarnings("unused")
  private boolean intakeBoolean = true;

  @SuppressWarnings("unused")
  private Command autoShoot = null;

  @SuppressWarnings("unused")
  private Command reverseMotors = null;

  // PS5 button aliases
  @SuppressWarnings("unused")
  private final Button CROSS = Button.A;

  @SuppressWarnings("unused")
  private final Button CIRCLE = Button.B;

  @SuppressWarnings("unused")
  private final Button SQUARE = Button.X;

  @SuppressWarnings("unused")
  private final Button TRIANGLE = Button.Y;

  @SuppressWarnings("unused")
  private final Button LB = Button.LB;

  private final Button RB = Button.RB;
  private final Button CREATE = Button.BACK;

  @SuppressWarnings("unused")
  private final Button OPTIONS = Button.START;

  @SuppressWarnings("unused")
  private final Button LEFT_JOY = Button.LEFT_JOY;

  private final Button RIGHT_JOY = Button.RIGHT_JOY;

  // PS5 trigger buttons
  @SuppressWarnings("unused")
  private final BooleanSupplier LEFT_TRIGGER_BUTTON = controller.LEFT_TRIGGER_BUTTON;

  @SuppressWarnings("unused")
  private final BooleanSupplier RIGHT_TRIGGER_BUTTON = controller.RIGHT_TRIGGER_BUTTON;

  // PS5 axis aliases
  private final Axis LEFT_X = Axis.LEFT_X;
  private final Axis LEFT_Y = Axis.LEFT_Y;
  private final Axis RIGHT_X = Axis.RIGHT_X;
  private final Axis RIGHT_Y = Axis.RIGHT_Y;

  // private final Axis LEFT_TRIGGER = Axis.LEFT_TRIGGER;
  // private final Axis RIGHT_TRIGGER = Axis.RIGHT_TRIGGER;

  public PS5XboxModeDriverConfig(Drivetrain drive) {
    super(drive);
  }

  public void configureControls() {
    // Reset the yaw. Mainly useful for testing/driver practice
    controller
        .get(CREATE)
        .onTrue(
            new InstantCommand(
                () ->
                    getDrivetrain()
                        .setYaw(
                            new Rotation2d(Robot.getAlliance() == Alliance.BLUE ? 0 : Math.PI))));

    // Cancel commands
    controller
        .get(RB)
        .onTrue(
            new InstantCommand(
                () -> {
                  getDrivetrain().setIsAlign(false);
                  getDrivetrain().setDesiredPose(() -> null);
                  CommandScheduler.getInstance().cancelAll();
                }));

    // Align wheels
    controller
        .get(DPad.RIGHT)
        .onTrue(
            new FunctionalCommand(
                    () -> getDrivetrain().setStateDeadband(false),
                    getDrivetrain()::alignWheels,
                    interrupted -> getDrivetrain().setStateDeadband(true),
                    () -> false,
                    getDrivetrain())
                .withTimeout(2));

    // Rumble test
    controller
        .get(RIGHT_JOY)
        .onTrue(
            new SequentialCommandGroup(
                new InstantCommand(
                    () -> controller.setRumble(GameController.RumbleStatus.RUMBLE_ON)),
                new WaitCommand(0.5),
                new InstantCommand(
                    () -> controller.setRumble(GameController.RumbleStatus.RUMBLE_OFF))));
  }

  @Override
  public double getRawSideTranslation() {
    return controller.get(LEFT_X);
  }

  @Override
  public double getRawForwardTranslation() {
    return controller.get(LEFT_Y);
  }

  @Override
  public double getRawRotation() {
    return controller.get(RIGHT_X);
  }

  @Override
  public double getRawHeadingAngle() {
    return Math.atan2(controller.get(RIGHT_X), -controller.get(RIGHT_Y)) - Math.PI / 2;
  }

  @Override
  public double getRawHeadingMagnitude() {
    return Math.hypot(controller.get(RIGHT_X), controller.get(RIGHT_Y));
  }

  @Override
  public boolean getIsSlowMode() {
    return slowModeSupplier.getAsBoolean();
  }

  @Override
  public boolean getIsAlign() {
    return false;
  }

  public void startRumble() {
    controller.setRumble(GameController.RumbleStatus.RUMBLE_ON);
  }

  public void endRumble() {
    controller.setRumble(GameController.RumbleStatus.RUMBLE_OFF);
  }
}
