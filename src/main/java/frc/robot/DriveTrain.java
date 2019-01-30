package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

import edu.wpi.first.wpilibj.drive.MecanumDrive;


public class DriveTrain {

    CANSparkMax leftFrontMotor;
    // Spark rightFrontMotor;
    // Spark leftBackMotor;
    // Spark rightBackMotor;
    MecanumDrive ckDrive;

    private DriveTrain (){  //so no duplicate subsystem is created.
        leftFrontMotor = new CANSparkMax(RMap.CANLeftFrontMotor, CANSparkMaxLowLevel.MotorType.kBrushless);
        // rightFrontMotor = new Spark(RMap.pwmRightFrontMotor);
        // leftBackMotor = new Spark(RMap.pwmLeftBackMotor);
        // rightBackMotor = new Spark(RMap.pwmRightBackMotor);

        //ckDrive = new MecanumDrive(leftFrontMotor, rightFrontMotor, leftBackMotor, rightBackMotor);
    }

    public void teleDriveCartesian (double forward, double rotation, double strafe){
        //ckDrive.driveCartesian(forward, rotation, strafe, 0);
    }
}
