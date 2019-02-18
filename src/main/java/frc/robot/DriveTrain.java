package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;

public class DriveTrain {

  CANSparkMax leftFrontMotor;
  CANSparkMax rightFrontMotor;
  CANSparkMax leftBackMotor;
  CANSparkMax rightBackMotor;
  MecanumDrive ckDrive;
  ADXRS450_Gyro ckGyro;
  Double speedF = 0.0;
  Double speedR = 0.0;
  Double speedS = 0.0;

  public DriveTrain() {
    leftFrontMotor = new CANSparkMax(RMap.CANLeftFrontMotor, CANSparkMaxLowLevel.MotorType.kBrushless);
    rightFrontMotor = new CANSparkMax(RMap.CANRightFrontMotor, CANSparkMaxLowLevel.MotorType.kBrushless);
    leftBackMotor = new CANSparkMax(RMap.CANLeftBackMotor, CANSparkMaxLowLevel.MotorType.kBrushless);
    rightBackMotor = new CANSparkMax(RMap.CANRightBackMotor, CANSparkMaxLowLevel.MotorType.kBrushless);

    ckDrive = new MecanumDrive(leftFrontMotor, leftBackMotor, rightFrontMotor, rightBackMotor);

    // Create the Gyro
    ckGyro = new ADXRS450_Gyro();

    SmartDashboard.putData("Drive Train", ckDrive);
  }

  public void teleDriveCartesian(double forward, double rotation, double strafe) {

    speedF = applyDappen(applyDeadBand(forward), speedF);
    speedR = applyDappen(applyDeadBand(rotation), speedR);
    speedS = applyDappen(applyDeadBand(strafe), speedS);

    //ckDrive.driveCartesian(applyDeadBand(strafe), applyDeadBand(forward), applyDeadBand(rotation), 0); // X is FORWARD!!!
    ckDrive.driveCartesian(speedS, speedF, speedR, 0); // X is FORWARD!!!
  }

  public void resetGyro() {
    ckGyro.reset();
  }

  public void driveStraight(double forward) {
    // TODO - Do a try catch here in case gyro is broken, otherwise return 0 for
    // turn amount
    double turnAmount = ckGyro.getAngle() * RMap.gyroStraightKp;
    ckDrive.driveCartesian(0, forward, turnAmount);
  }

  // Apply a RMap.driveTrainDeadzone because wpilib doensn't like rotation
  // RMap.driveTrainDeadzone
  private double applyDeadBand(double value) {
    if (Math.abs(value) > RMap.driveTrainDeadzone) {
      if (value > 0.0) {
        return (value - RMap.driveTrainDeadzone) / (1.0 - RMap.driveTrainDeadzone);
      } else {
        return (value + RMap.driveTrainDeadzone) / (1.0 - RMap.driveTrainDeadzone);
      }
    } else {
      return 0.0;
    }
  }

  public double applyDappen(double proposedValue, double currentSpeed) {
    // Apply Dapening
    if (currentSpeed > 0.0) {
      // Going Forward
      if (proposedValue > currentSpeed) {
        // Accelerating Forward
        proposedValue = Math.min(proposedValue, currentSpeed + RMap.maxSpeedIncrease);
      } else {
        // Decelerating Forward
        proposedValue = Math.min(proposedValue, currentSpeed - RMap.maxSpeedDecrease);
      }
    } else {
      // Going Backwards
      if (proposedValue < currentSpeed) {
        // Accelerating Backwards
        proposedValue = Math.max(proposedValue, currentSpeed - RMap.maxSpeedIncrease);
      } else {
        // Decelerating Backwards
        proposedValue = Math.max(proposedValue, currentSpeed + RMap.maxSpeedDecrease);
      }
    }

    return proposedValue;
  }
}
