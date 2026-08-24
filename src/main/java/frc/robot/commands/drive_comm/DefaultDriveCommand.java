package frc.robot.commands.drive_comm;

import org.wpilib.math.controller.PIDController;
import org.wpilib.math.kinematics.ChassisVelocities;
import org.wpilib.driverstation.DriverStation.Alliance;
import org.wpilib.smartdashboard.SmartDashboard;
import org.wpilib.command2.Command;
import frc.robot.Robot;
import frc.robot.constants.Constants;
import frc.robot.constants.swerve.DriveConstants;
import frc.robot.controls.BaseDriverConfig;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.util.Vision.DriverAssist;

/** Default drive command. Drives robot using driver controls. */
public class DefaultDriveCommand extends Command {
  protected final Drivetrain swerve;
  private final BaseDriverConfig driver;
  private PIDController trenchAssistPid = new PIDController(9, 0.0, 3);

  public DefaultDriveCommand(Drivetrain swerve, BaseDriverConfig driver) {
    this.swerve = swerve;
    this.driver = driver;

    addRequirements(swerve);
  }

  @Override
  public void initialize() {
    swerve.setStateDeadband(true);

    trenchAssistPid.setIZone(2);
    trenchAssistPid.setIntegratorRange(-1, 1);

    if (!Constants.DISABLE_SMART_DASHBOARD) {
      SmartDashboard.putNumber("0 degrees snap location", 0);
    }
  }

  @Override
  public void execute() {
    double forwardTranslation = driver.getForwardTranslation();
    double sideTranslation = driver.getSideTranslation();
    double rotation = -driver.getRotation();

    double slowFactor = driver.getIsSlowMode() ? DriveConstants.SLOW_DRIVE_FACTOR : 1;

    forwardTranslation *= slowFactor;
    sideTranslation *= slowFactor;
    rotation *= driver.getIsSlowMode() ? DriveConstants.SLOW_ROT_FACTOR : 1;

    int allianceReversal = Robot.getAlliance() == Alliance.Red ? 1 : -1;
    forwardTranslation *= allianceReversal;
    sideTranslation *= allianceReversal;

    ChassisVelocities driverInput = new ChassisVelocities(forwardTranslation, sideTranslation, rotation);
    ChassisVelocities corrected =
        DriverAssist.calculate(swerve, driverInput, swerve.getDesiredPose(), true);
  }

  /**
   * Drives the robot
   *
   * @param speeds The ChassisVelocities to drive at
   */
  protected void drive(ChassisVelocities speeds) {
    // If the driver is pressing the align button or a command set the drivetrain to
    // align, then align to speaker
    if (driver.getIsAlign() || swerve.getIsAlign()) {
      swerve.driveHeading(
          speeds.vx, speeds.vy, swerve.getAlignAngle(), true);
    } else {
      swerve.drive(
          speeds.vx,
          speeds.vy,
          speeds.omega,
          true,
          false);
    }
  }
}
