package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class RMap {

    //Pixy
    static int PixySPIPort = 4; //mxp SPI port on rio

    //Speed controllers
    static int CANLeftFrontMotor = 10;
    static int CANRightFrontMotor = 11;
    static int CANLeftBackMotor = 12;
    static int CANRightBackMotor = 13;

    //vision
    static int pixelLeft = 320; //left x middle pixel
    static int pixelRight = 321; //right x middle pixel 

    //ballShooter
    static int conveyor = 1;

    //Hatch Arm
    static DoubleSolenoid.Value armCANUp = DoubleSolenoid.Value.kForward;
    static DoubleSolenoid.Value armCANDown = DoubleSolenoid.Value.kReverse;

    public static int pcmArmUp = 1;
    public static int pcmArmDown = 2;

}
