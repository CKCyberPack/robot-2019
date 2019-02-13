package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class RMap {
    
    //Pixy
    static int PixySPIPort = 4; //mxp SPI port on rio

    //Speed controllers
    static int CANLeftFrontMotor = 12;
    static int CANRightFrontMotor = 11;
    static int CANLeftBackMotor = 13;
    static int CANRightBackMotor = 10;

    //Vision
    static int cameraXMid = 210; //midpoint of camera 
    static int cameraXDeadZone = 30;
    static int cameraBlockWidth = 10; //width of block

    //BallShooter
    static int CANConveyor = 20;
    static double conveyorSpeedIn = 0.5;
    static double conveyorSpeedOut = 1;

    //Hatch Arm
    static DoubleSolenoid.Value pcmForward = DoubleSolenoid.Value.kForward;
    static DoubleSolenoid.Value pcmReverse = DoubleSolenoid.Value.kReverse;
    static int pcmArmUp = 4;
    static int pcmArmDown = 1;
    static int pcmArmTurnIn = 6;
    static int pcmArmTurnOut = 7;
    static int pcmArmFingersIn = 2;
    static int pcmArmFingersOut = 3;

    //Ramp
    static int pcmRampUp = 0;
    static int pcmRampDown = 5;

}
