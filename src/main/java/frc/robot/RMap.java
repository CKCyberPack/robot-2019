package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class RMap {
    
    //Pixy
    static int PixySPIPort = 4; //mxp SPI port on rio

    //Drive Train
    static int CANLeftFrontMotor = 12;
    static int CANRightFrontMotor = 11;
    static int CANLeftBackMotor = 13;
    static int CANRightBackMotor = 10;
    static double driveTrainDeadzone = 0.05;

    //Vision
    static int cameraXMid = 210; //midpoint of camera 
    static int cameraXDeadZone = 30;
    static int cameraBlockWidth = 10; //width of block
    static double cameraDriveSlow = 0.05;
    static double cameraDriveFast = 0.1;
    static double cameraDriveStrafeRight = 0.05;
    static double cameraDriveStrafeLeft = -0.05;
    static double cameraDriveTurnRight = 0.05;
    static double cameraDriveTurnLeft = -0.05;

    //BallShooter
    static int CANConveyor = 20;
    static double conveyorSpeedIn = 0.5;
    static double conveyorSpeedOut = 1;

    //Hatch Arm
    static DoubleSolenoid.Value pcmForward = DoubleSolenoid.Value.kForward;
    static DoubleSolenoid.Value pcmReverse = DoubleSolenoid.Value.kReverse;
    static int pcmArmUp = 3; 
    static int pcmArmDown = 2;
    static int pcmArmTurn = 7;
    //static int pcmArmTurnOut = 7;
    static int pcmArmFingersIn = 1;
    static int pcmArmFingersOut = 4;

    //Platform
    static int pcmPlatformUp = 0;
    static int pcmPlatformDown = 5;

}
