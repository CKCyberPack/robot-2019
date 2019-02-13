package frc.robot;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

import com.ctre.phoenix.motorcontrol.ControlMode;
//TODO: import libraries for VictorSPX (CTRE)
import com.ctre.phoenix.motorcontrol.can.VictorSPX; //download phenix framework

/*
 * We need to import the libraries for the VictorSPX (CTRE Pheonix API according to last year)
 * Once the lib is added, remove comments in setSpeed and stopMotor
 */
public class BallShooter {
    VictorSPX conveyVictorSPX;
    Solenoid armSolenoid;

    public BallShooter() {
        conveyVictorSPX = new VictorSPX (RMap.conveyor);
        
    }

    public void setSpeed(double speed) {
        conveyVictorSPX.set(ControlMode.PercentOutput, speed);
    }

    public void stopMotor(){
        conveyVictorSPX.set(ControlMode.Disabled, 0);
    }



}