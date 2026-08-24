package frc.robot.controls;

import java.util.function.BooleanSupplier;

import org.wpilib.math.geometry.Rotation2d;
import org.wpilib.driverstation.Alliance;
import org.wpilib.command2.CommandScheduler;
import org.wpilib.command2.InstantCommand;
import frc.robot.Robot;
import frc.robot.constants.Constants;
import frc.robot.subsystems.drivetrain.Drivetrain;
import lib.controllers.PS5Controller;
import lib.controllers.PS5Controller.PS5Axis;
import lib.controllers.PS5Controller.PS5Button;

/** Driver controls for the PS5 controller */
public class PS5ControllerDriverConfig extends BaseDriverConfig {
  private final PS5Controller controller = new PS5Controller(Constants.DRIVER_JOY);
  private final BooleanSupplier slowModeSupplier = () -> false;

  public PS5ControllerDriverConfig(Drivetrain drive) {
    super(drive);
  }

  public void configureControls() {
    // Reset the yaw. Mainly useful for testing/driver practice
    controller
        .get(PS5Button.CREATE)
        .onTrue(
            new InstantCommand(
                () ->
                    getDrivetrain()
                        .setYaw(
                            new Rotation2d(Robot.getAlliance() == Alliance.BLUE ? 0 : Math.PI))));

    // Cancel commands
    controller
        .get(PS5Button.RB)
        .onTrue(
            new InstantCommand(
                () -> {
                  getDrivetrain().setIsAlign(false);
                  getDrivetrain().setDesiredPose(() -> null);
                  CommandScheduler.getInstance().cancelAll();
                }));
  }

  @Override
  public double getRawSideTranslation() {
    return controller.get(PS5Axis.LEFT_X);
  }

  @Override
  public double getRawForwardTranslation() {
    return controller.get(PS5Axis.LEFT_Y);
  }

  @Override
  public double getRawRotation() {
    return controller.get(PS5Axis.RIGHT_X);
  }

  @Override
  public double getRawHeadingAngle() {
    return Math.atan2(controller.get(PS5Axis.RIGHT_X), -controller.get(PS5Axis.RIGHT_Y))
        - Math.PI / 2;
  }

  @Override
  public double getRawHeadingMagnitude() {
    return Math.hypot(controller.get(PS5Axis.RIGHT_X), controller.get(PS5Axis.RIGHT_Y));
  }

  @Override
  public boolean getIsSlowMode() {
    return slowModeSupplier.getAsBoolean();
  }

  @Override
  public boolean getIsAlign() {
    return false;
  }
}
