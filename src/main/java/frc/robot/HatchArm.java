package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;

public class HatchArm{

    private DoubleSolenoid armSolenoid;
    
    public enum Position{
        Up,
        Down
    }
    public HatchArm(){
        armSolenoid = new DoubleSolenoid(RMap.pcmArmUp, RMap.pcmArmDown);
    }

    public void Grab(Position pos){
        switch(pos){
            case Up:
                armSolenoid.set(RMap.armCANUp);
                break;
            case Down:
                armSolenoid.set(RMap.armCANDown);
                break;
        }

    }

}