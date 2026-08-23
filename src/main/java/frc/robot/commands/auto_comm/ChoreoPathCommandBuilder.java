package frc.robot.commands.auto_comm;

import choreo.auto.AutoFactory;
import org.wpilib.command2.Command;
import org.wpilib.command2.Commands;
import org.wpilib.command2.InstantCommand;
import frc.robot.commands.DoNothing;

public class ChoreoPathCommandBuilder {

  public ChoreoPathCommandBuilder() {}

  public static Command basicTrajectoryAuto(
      String pathName, boolean resetOdemetry, AutoFactory factory) {
    Command command = factory.trajectoryCmd(pathName);

    return Commands.sequence(
        resetOdemetry ? new InstantCommand(() -> factory.resetOdometry(pathName)) : new DoNothing(),
        command);
  }
}
