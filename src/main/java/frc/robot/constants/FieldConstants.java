package frc.robot.constants;

import org.wpilib.vision.apriltag.AprilTagFieldLayout;
import org.wpilib.vision.apriltag.AprilTagFields;

public class FieldConstants {

  /** Apriltag layout for 2026 REBUILT */
  public static final AprilTagFieldLayout field =
      AprilTagFieldLayout.loadField(AprilTagFields.k2026RebuiltWelded);

  /** Width of the field [meters] */
  public static final double FIELD_LENGTH = field.getFieldLength();

  /** Height of the field [meters] */
  public static final double FIELD_WIDTH = field.getFieldWidth();
}
