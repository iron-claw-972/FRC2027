package frc.robot.commands;

import org.wpilib.command2.Command;
import frc.robot.constants.Constants;

/// Command for logging stuff
public class LogCommand extends Command {
  @SuppressWarnings("unused")
  private boolean hubActive = false;

  public LogCommand() {}

  @Override
  public void execute() {
    if (Constants.DISABLE_LOGGING) {
      return;
    }
  }

  @Override
  public boolean runsWhenDisabled() {
    return true;
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
