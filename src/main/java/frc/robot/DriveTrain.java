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

    public DriveTrain() {
        leftFrontMotor = new CANSparkMax(RMap.CANLeftFrontMotor, CANSparkMaxLowLevel.MotorType.kBrushless);
        rightFrontMotor = new CANSparkMax(RMap.CANRightFrontMotor, CANSparkMaxLowLevel.MotorType.kBrushless);
        leftBackMotor = new CANSparkMax(RMap.CANLeftBackMotor, CANSparkMaxLowLevel.MotorType.kBrushless);
        rightBackMotor = new CANSparkMax(RMap.CANRightBackMotor, CANSparkMaxLowLevel.MotorType.kBrushless);

        ckDrive = new MecanumDrive(leftFrontMotor, leftBackMotor, rightFrontMotor, rightBackMotor);

        //Create the Gyro
        ckGyro = new ADXRS450_Gyro();

        SmartDashboard.putData("Drive Train", ckDrive);
    }

    public void teleDriveCartesian(double forward, double rotation, double strafe) {
        ckDrive.driveCartesian(applyDeadBand(strafe), applyDeadBand(forward), applyDeadBand(rotation), 0); //X is FORWARD!!!
        // SmartDashboard.putNumber("Left Front", leftFrontMotor.get());
        // SmartDashboard.putNumber("Right Front", rightFrontMotor.get());
        // SmartDashboard.putNumber("Left Back", leftBackMotor.get());
        // SmartDashboard.putNumber("Right Back", rightBackMotor.get());
    }


    public void resetGyro(){
      ckGyro.reset();
    }


    public void driveStraight(double forward){
      //TODO - Do a try catch here in case gyro is broken, otherwise return 0 for turn amount
      double turnAmount = ckGyro.getAngle() * RMap.gyroStraightKp;
      ckDrive.driveCartesian(0, forward, turnAmount);
    }

    //Apply a RMap.driveTrainDeadzone because wpilib doensn't like rotation RMap.driveTrainDeadzone
  private double applyDeadBand (double value) {
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
}
