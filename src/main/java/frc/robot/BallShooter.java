package frc.robot;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

//TODO: import libraries for VictorSPX (CTRE)


/*
 * We need to import the libraries for the VictorSPX (CTRE Pheonix API according to last year)
 * Once the lib is added, remove comments in setSpeed and stopMotor
 */
public class BallShooter {
    VictorSP conveyVictorSP;
    Solenoid armSolenoid;

    public BallShooter() {
        conveyVictorSP = new VictorSP (RMap.conveyor);
        
    }

    public void setSpeed(double speed) {
        // conveyVictorSP.set(ControlMode.percentOutput, speed);
    }

    public void stopMotor(){
        // conveyVictorSP.set(ControlMode.disable, 0);
    }


}