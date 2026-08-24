package frc.robot.commands.drive_comm;

import org.wpilib.math.geometry.Rotation2d;
import org.wpilib.math.kinematics.SwerveModuleVelocity;
import org.wpilib.math.util.Units;
import org.wpilib.command2.InstantCommand;
import org.wpilib.command2.RunCommand;
import org.wpilib.command2.SequentialCommandGroup;
import frc.robot.subsystems.drivetrain.Drivetrain;

/** Sets the robot's wheels to an X formation to prevent being pushed around by other bots. */
public class SetFormationX extends SequentialCommandGroup {
  public SetFormationX(Drivetrain drive) {
    addRequirements(drive);
    addCommands(
        new InstantCommand(() -> drive.setStateDeadband(false), drive),
        new RunCommand(
            () ->
                drive.setModuleStates(
                    new SwerveModuleVelocity[] {
                      new SwerveModuleVelocity(0, new Rotation2d(Units.degreesToRadians(45))),
                      new SwerveModuleVelocity(0, new Rotation2d(Units.degreesToRadians(-45))),
                      new SwerveModuleVelocity(0, new Rotation2d(Units.degreesToRadians(-45))),
                      new SwerveModuleVelocity(0, new Rotation2d(Units.degreesToRadians(45)))
                    },
                    false),
            drive));
  }
}
