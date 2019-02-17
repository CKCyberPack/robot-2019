package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Platform{

    private DoubleSolenoid platformPiston;

    public enum PlatformPosition{
        Up,
        Down
    }
    public Platform(){
        platformPiston = new DoubleSolenoid(RMap.pcmPlatformUp, RMap.pcmPlatformDown);
    }
    public void launchPlatform(PlatformPosition pos){
        switch(pos){
            case Up:
                platformPiston.set(RMap.pcmForward);
                break;
            case Down:
                platformPiston.set(RMap.pcmReverse);
                break;
        }
    }
    
}