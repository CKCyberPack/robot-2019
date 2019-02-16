package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Ramp{

    private DoubleSolenoid rampPiston;

    public enum RampPosition{
        Up,
        Down
    }
    public Ramp(){
        rampPiston = new DoubleSolenoid(RMap.pcmRampUp, RMap.pcmRampDown);
    }
    public void launchRamp(RampPosition pos){
        switch(pos){
            case Up:
                rampPiston.set(RMap.pcmForward);
                break;
            case Down:
                rampPiston.set(RMap.pcmReverse);
                break;
        }
    }
    
}