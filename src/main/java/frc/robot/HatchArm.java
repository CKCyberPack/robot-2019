package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;

public class HatchArm{

    private DoubleSolenoid armPiston;
    private DoubleSolenoid armPistonTurn;
    private DoubleSolenoid armPistonFinger;
    private ArmPosition currentArmPosition;

    public enum ArmPosition{
        Up,
        Down,
        In,
        Out
    }
    public enum FingerPosition{
        In,
        Out
    }
    public HatchArm(){
        armPiston = new DoubleSolenoid(RMap.pcmArmUp, RMap.pcmArmDown);
        armPistonTurn = new DoubleSolenoid(RMap.pcmArmTurnIn, RMap.pcmArmTurnOut);
        armPistonFinger = new DoubleSolenoid(RMap.pcmArmFingersIn, RMap.pcmArmFingersOut);
    }
    public void fireArm(ArmPosition pos){
        switch(pos){
            case Up:
                armPiston.set(RMap.pcmForward);
                currentArmPosition = pos;
                break;
            case Down:
                armPiston.set(RMap.pcmReverse);
                currentArmPosition = pos;
                break;
            case In:
                armPistonTurn.set(RMap.pcmForward);
                break;
            case Out:
                armPistonTurn.set(RMap.pcmReverse);
                break;
        }
    }
    public void fireFinger(FingerPosition pos){
        switch(pos){
            case In:
                armPistonFinger.set(RMap.pcmForward);
                break;
            case Out:
                armPistonFinger.set(RMap.pcmReverse);
                break;
        }
    }
    public void toggleArm(XboxController myController){
        if (currentArmPosition == ArmPosition.Up){
            fireArm(ArmPosition.Down);
            myController.setRumble(RumbleType.kRightRumble, 1); //turn rumble on
        }
        else{
            fireArm(ArmPosition.Up);
            myController.setRumble(RumbleType.kRightRumble, 0); //turns rumble off
        }
    }

}