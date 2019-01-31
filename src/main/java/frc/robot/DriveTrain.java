package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

import edu.wpi.first.wpilibj.drive.MecanumDrive;


public class DriveTrain {

    CANSparkMax leftFrontMotor;
    CANSparkMax rightFrontMotor;
    CANSparkMax leftBackMotor;
    CANSparkMax rightBackMotor;
    MecanumDrive ckDrive;

    private DriveTrain (){  //so no duplicate subsystem is created.
        leftFrontMotor = new CANSparkMax(RMap.CANLeftFrontMotor, CANSparkMaxLowLevel.MotorType.kBrushless);
        rightFrontMotor = new CANSparkMax(RMap.CANRightFrontMotor, CANSparkMaxLowLevel.MotorType.kBrushless);
        leftBackMotor = new CANSparkMax(RMap.CANLeftBackMotor, CANSparkMaxLowLevel.MotorType.kBrushless);
        rightBackMotor = new CANSparkMax(RMap.CANRightBackMotor, CANSparkMaxLowLevel.MotorType.kBrushless);

        ckDrive = new MecanumDrive(leftFrontMotor, rightFrontMotor, leftBackMotor, rightBackMotor);
    }

    public void teleDriveCartesian (double forward, double rotation, double strafe){
        //ckDrive.driveCartesian(forward, rotation, strafe, 0);
    }
}
