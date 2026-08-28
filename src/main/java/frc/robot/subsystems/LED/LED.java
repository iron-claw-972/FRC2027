package frc.robot.subsystems.LED;

import com.ctre.phoenix6.configs.CANdleConfigurator;
import com.ctre.phoenix6.configs.CANdleFeaturesConfigs;
import com.ctre.phoenix6.configs.LEDConfigs;
import com.ctre.phoenix6.controls.ColorFlowAnimation;
import com.ctre.phoenix6.controls.FireAnimation;
import com.ctre.phoenix6.controls.RainbowAnimation;
import com.ctre.phoenix6.controls.RgbFadeAnimation;
import com.ctre.phoenix6.controls.SolidColor;
import com.ctre.phoenix6.controls.StrobeAnimation;
import com.ctre.phoenix6.controls.TwinkleAnimation;
import com.ctre.phoenix6.hardware.CANdle;
import com.ctre.phoenix6.signals.Enable5VRailValue;
import com.ctre.phoenix6.signals.LossOfSignalBehaviorValue;
import com.ctre.phoenix6.signals.RGBWColor;
import com.ctre.phoenix6.signals.StatusLedWhenActiveValue;
import com.ctre.phoenix6.signals.StripTypeValue;
import com.ctre.phoenix6.signals.VBatOutputModeValue;

import org.wpilib.driverstation.internal.DriverStationBackend;
import org.wpilib.driverstation.Alliance;
import org.wpilib.util.Color;
import org.wpilib.command2.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.constants.IdConstants;

public class LED extends SubsystemBase {

  private CANdle candle;
  public static final int stripLength = 67;

  /// Hz
  public static final int FLASH_RATE = 4;

  private Color color;

  public LED() {
    candle = new CANdle(IdConstants.CANDLE_ID, Constants.RIO_CAN);
    CANdleConfigurator configurator = candle.getConfigurator();

    LEDConfigs ledConf =
        new LEDConfigs()
            .withStripType(StripTypeValue.GRB)
            .withLossOfSignalBehavior(LossOfSignalBehaviorValue.KeepRunning)
            .withBrightnessScalar(1);

    CANdleFeaturesConfigs featureConf =
        new CANdleFeaturesConfigs()
            .withEnable5VRail(Enable5VRailValue.Enabled) // Turns off LEDs
            .withStatusLedWhenActive(StatusLedWhenActiveValue.Disabled)
            .withVBatOutputMode(VBatOutputModeValue.On);

    configurator.apply(featureConf);
    configurator.apply(ledConf);

    setColor();

    candle.clearAllAnimations();
    lightsOff();

    // System.out.println("CANdle features: " + featureConf + ", LED config: " + ledConf);
  }

  public void setColor() {
    var alliance = DriverStationBackend.getAlliance();
    if (alliance.isEmpty()) {
      color = Color.ORANGE_RED;
    } else if (alliance.get() == Alliance.RED) {
      color = Color.RED;
    } else if (alliance.get() == Alliance.BLUE) {
      color = Color.BLUE;
    } else {
      color = Color.ORANGE_RED;
    }
  }

  private enum State {
    OFF,
    ON,
    AUTO,
    SLOW,
    FAST,
    ENDGAME
  };

  private State lastState = State.OFF;
  private boolean forceOff = false;

  @Override
  public void periodic() {
    State targetState = State.ON;
    // if (underSecsToFlip(5)) targetState = State.SLOW;
    // if (underSecsToFlip(1)) targetState = State.FAST;
    if (DriverStationBackend.isAutonomous()) targetState = State.AUTO;
    if (DriverStationBackend.getMatchTime() < 30) targetState = State.ENDGAME;
    if (forceOff) targetState = State.OFF;

    if (targetState != lastState) {
      switch (targetState) {
        case OFF:
          lightsOff();
          break;
        case ON:
          setStatic();
          break;
        case AUTO:
          setTwinkle();
          break;
        case SLOW:
          setStrobe();
          break;
        case FAST:
          setFastStrobe();
          break;
        case ENDGAME:
          setRainbow();
          break;
      }
      lastState = targetState;
    }
  }

  public void setFire() {
    candle.clearAllAnimations();
    candle.setControl(new FireAnimation(8, 8 + stripLength).withSparking(0.5));
  }

  public void setRainbow() {
    candle.clearAllAnimations();
    candle.setControl(new RainbowAnimation(8, 8 + stripLength));
  }

  public void setRgbFadeAnimation() {
    candle.clearAllAnimations();
    candle.setControl(new RgbFadeAnimation(8, 8 + stripLength));
  }

  public void setTwinkle() {
    candle.clearAllAnimations();
    candle.setControl(
        new TwinkleAnimation(8, 8 + stripLength).withColor(new RGBWColor(Color.VIOLET)));
  }

  public void setColorFlow() {
    candle.clearAllAnimations();
    candle.setControl(
        new ColorFlowAnimation(8, 8 + stripLength).withColor(new RGBWColor(Color.AZURE)));
  }

  public void setStrobe() {
    candle.clearAllAnimations();
    candle.setControl(
        new StrobeAnimation(8, 8 + stripLength)
            .withFrameRate(FLASH_RATE)
            .withColor(new RGBWColor(color)));
  }

  public void setFastStrobe() {
    candle.clearAllAnimations();
    candle.setControl(
        new StrobeAnimation(8, 8 + stripLength)
            .withFrameRate(FLASH_RATE * 4)
            .withColor(new RGBWColor(color)));
  }

  public void setStatic() {
    candle.clearAllAnimations();
    candle.setControl(new SolidColor(8, 8 + stripLength).withColor(new RGBWColor(color)));
  }

  public void lightsOff() {
    candle.clearAllAnimations();
    candle.setControl(new SolidColor(8, 8 + stripLength).withColor(new RGBWColor(0, 0, 0, 0)));
  }
}
