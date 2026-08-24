package frc.robot.commands.drive_comm;

import org.wpilib.math.geometry.Pose2d;
import org.wpilib.math.kinematics.ChassisVelocities;
import org.wpilib.math.kinematics.SwerveModuleVelocity;
import org.wpilib.math.trajectory.Trajectory;
import org.wpilib.math.trajectory.Trajectory.State;
import org.wpilib.command2.InstantCommand;
import frc.robot.constants.swerve.DriveConstants;
import frc.robot.subsystems.drivetrain.Drivetrain;

/** Sets all module angles to a given trajectory's initial angle. */
public class TrajectoryPresetSteerAngles extends InstantCommand {
  /*
   * make sure to add wait command after called to give time to correct
   */
  public TrajectoryPresetSteerAngles(Drivetrain drive, Trajectory trajectory) {
    super(
        () -> {

          // 0.01 is the time between trajectory samples, in seconds
          // Can be replaced for any small number, but it should be the same as the time between all
          // uses
          double time = 0.01;

          drive.setStateDeadband(false);

          Pose2d initialPose = trajectory.getInitialPose();
          State sample = trajectory.sample(time);
          Pose2d nextPose = sample.pose;

          double xVelocity = sample.velocity * nextPose.getRotation().getCos();
          double yVelocity = sample.velocity * nextPose.getRotation().getSin();
          double angularVelo =
              (nextPose.getRotation().getRadians() - initialPose.getRotation().getRadians()) / time;

          ChassisVelocities chassisSpeeds = new ChassisVelocities(xVelocity, yVelocity, angularVelo);
          chassisSpeeds =
              ChassisVelocities.fromFieldRelativeSpeeds(chassisSpeeds, initialPose.getRotation());

          SwerveModuleVelocity[] SwerveModuleVelocitys =
              DriveConstants.KINEMATICS.toSwerveModuleVelocities(chassisSpeeds);
          for (SwerveModuleVelocity SwerveModuleVelocity : SwerveModuleVelocitys) {
            SwerveModuleVelocity.speedMetersPerSecond = 0;
          }
          drive.setModuleStates(SwerveModuleVelocitys, true);
          drive.setStateDeadband(true);
        },
        drive);
  }
}
