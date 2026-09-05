package frc.robot.subsystems;

import org.wpilib.command2.SubsystemBase;

import com.ctre.phoenix6.hardware.TalonFX;

import frc.robot.constants.Constants;
import frc.robot.constants.IdConstants;

public class Motor extends SubsystemBase {
    TalonFX motor = new TalonFX(56, Constants.CANIVORE_SUB);
    public Motor() {
        System.out.println("wassup");

    }

    @Override
    public void periodic() {
        spin(.1);
    }

    public void spin(double speed) {
        motor.setThrottle(speed);
    }
    
}
