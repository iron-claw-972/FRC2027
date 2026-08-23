package frc.robot.commands;

import com.ctre.phoenix6.Orchestra;
import com.ctre.phoenix6.hardware.TalonFX;

import org.wpilib.system.Filesystem;
import org.wpilib.command2.Command;

public class Music extends Command {
  private Orchestra orchestra;

  public Music(TalonFX[] motors) {
    orchestra = new Orchestra(Filesystem.getDeployDirectory() + "/chirp/file.chrp");
    for (TalonFX motor : motors) {
      System.out.println(motor.getDescription());
      orchestra.addInstrument(motor);
    }
  }

  @Override
  public void initialize() {
    orchestra.play();
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interrupted) {
    orchestra.stop();
  }

  @Override
  public boolean runsWhenDisabled() {
    return true;
  }
}
