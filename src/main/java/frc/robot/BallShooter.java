package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX; 

public class BallShooter {
    VictorSPX conveyVictorSPX;

    public BallShooter() {
        conveyVictorSPX = new VictorSPX (RMap.CANConveyor);
        
    }

    public void setSpeed(double speed) {
        conveyVictorSPX.set(ControlMode.PercentOutput, speed);
    }

    public void intakeBall() {
        setSpeed(RMap.conveyorSpeedIn);
    }

    public void shootBall() {
        setSpeed(RMap.conveyorSpeedOut);
    }

    public void stopMotor(){
        conveyVictorSPX.set(ControlMode.Disabled, 0);
    }

}