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

    //BallShooter
    static int conveyor = 1;

    //Hatch Arm
    static DoubleSolenoid.Value armCANUp = DoubleSolenoid.Value.kForward;
    static DoubleSolenoid.Value armCANDown = DoubleSolenoid.Value.kReverse;

    public static int pcmArmUp = 1;
    public static int pcmArmDown = 2;

}
