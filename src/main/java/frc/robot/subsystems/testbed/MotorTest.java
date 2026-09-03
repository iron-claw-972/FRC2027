package frc.robot.subsystems.testbed;

import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import org.wpilib.command2.SubsystemBase;
import org.wpilib.smartdashboard.SmartDashboard;
import frc.robot.constants.Constants;
import frc.robot.constants.IdConstants;

/**
 * Simple subsystem to verify that a motor spins on a test bed.
 */
public class MotorTest extends SubsystemBase {

  private static final String SPEED_KEY = "Test Bed/Motor Speed";
  private static final String CURRENT_KEY = "Test Bed/Motor Stator Current (A)";

  private final VoltageOut voltageRequest = new VoltageOut(0);
  private TalonFX motor = new TalonFX(IdConstants.MOTOR_TEST_ID, Constants.RIO_CAN);

  public MotorTest() {
    motor.stopMotor();

    if (!Constants.DISABLE_SMART_DASHBOARD) {
      SmartDashboard.putNumber(SPEED_KEY, 0.0);
    }
  }

  public void spin(double speed) {
    motor.setControl(voltageRequest.withOutput(Math.clamp(speed, -1.0, 1.0) * 12.0).withEnableFOC(true));
  }

  public void stop() {
    motor.stopMotor();
  }

  @Override
  public void periodic() {
    if (!Constants.DISABLE_SMART_DASHBOARD) {
      double speed = SmartDashboard.getNumber(SPEED_KEY, 0.0);
      SmartDashboard.putNumber(
          CURRENT_KEY, motor.getStatorCurrent().getValueAsDouble());
      spin(speed);
    } else {
      stop();
    }
  }
}
