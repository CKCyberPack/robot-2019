package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class HatchArm {

    private DoubleSolenoid armPiston;
    private Solenoid armPistonTurn;
    private DoubleSolenoid armPistonFinger;
    private ArmPosition currentArmPosition;
    private ArmPosition currentArmTurnPosition;

    public enum ArmPosition {
        Up, Down, In, Out
    }

    public enum FingerPosition {
        In, Out
    }

    public HatchArm() {
        armPiston = new DoubleSolenoid(RMap.pcmArmUp, RMap.pcmArmDown);
        armPistonTurn = new Solenoid(RMap.pcmArmTurn);
        armPistonFinger = new DoubleSolenoid(RMap.pcmArmFingersIn, RMap.pcmArmFingersOut);
    }

    private void fireArm(ArmPosition pos) {
        switch (pos) {
        case Up:
            armPiston.set(RMap.pcmForward);
            currentArmPosition = pos;
            SmartDashboard.putBoolean("Arm DOWN", false);
            break;
        case Down:
            armPiston.set(RMap.pcmReverse);
            currentArmPosition = pos;
            SmartDashboard.putBoolean("Arm DOWN", true);
            break;
        case In:
            armPistonTurn.set(false); // TODO: Use pcmForward or Reverse
            currentArmTurnPosition = pos;
            SmartDashboard.putBoolean("Arm OUT", false);
            break;
        case Out:
            armPistonTurn.set(true);
            currentArmTurnPosition = pos;
            SmartDashboard.putBoolean("Arm OUT", true);
            break;
        }
    }

    public void fireFinger(FingerPosition pos) {
        switch (pos) {
        case In:
            armPistonFinger.set(RMap.pcmForward);
            break;
        case Out:
            armPistonFinger.set(RMap.pcmReverse);
            break;
        }
    }

    public void toggleArm(XboxController myController) {
        // Do nothing if Arm is Inside the robot
        if (currentArmTurnPosition == ArmPosition.In) {
            return;
        }

        if (currentArmPosition == ArmPosition.Up) {
            fireArm(ArmPosition.Down);
            myController.setRumble(RumbleType.kRightRumble, 1); // turn rumble on
            myController.setRumble(RumbleType.kLeftRumble, 1); // turn rumble on
        } else {
            fireArm(ArmPosition.Up);
            myController.setRumble(RumbleType.kRightRumble, 0); // turns rumble off
            myController.setRumble(RumbleType.kLeftRumble, 0); // turns rumble off
        }
    }

    public void toggleArmTurn() {
        // Don't let it turn if the arm is down
        if (currentArmPosition == ArmPosition.Down) {
            return;
        }

        if (currentArmTurnPosition == ArmPosition.Out) {
            fireArm(ArmPosition.In);
        } else {
            fireArm(ArmPosition.Out);
        }
    }

    public void firePlatformPosition() {
        fireArm(ArmPosition.Out); // make sure arm can go down (turn it)
        fireArm(ArmPosition.Down); // make sure arm is out of the way
    }

    public void startingPosition() {
        // Arm Turn In
        fireArm(ArmPosition.In);
        // Start Arm UP
        fireArm(ArmPosition.Up);
        // Start fingers in
        fireFinger(FingerPosition.In);
    }

}