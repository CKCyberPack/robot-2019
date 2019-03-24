/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.ArrayList;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import io.github.pseudoresonance.pixy2api.Pixy2;
// import io.github.pseudoresonance.pixy2api.Pixy2CCC;
// import io.github.pseudoresonance.pixy2api.Pixy2CCC.Block;
import frc.robot.HatchArm.FingerPosition;
import frc.robot.Platform.PlatformPosition;
import edu.wpi.cscore.UsbCamera;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String auto_DriveForward = "DriveForward";
  private static final String auto_DoNothing = "NOTHING";
  private String auto_Selected;
  private final SendableChooser<String> autoChooser = new SendableChooser<>();

  //private Pixy2 ckPixy;

  // robot components
  private XboxController ckController;
  private PowerDistributionPanel ckPDP;
  private DriveTrain ckDrive;
  private BallShooter ckBall;
  private HatchArm ckArm;
  private Platform ckPlatform;
  private UsbCamera ckCameraFront;
  private UsbCamera ckCameraRear;

  private long startTimer;
  private long currentTimer;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    System.out.println("---Robot Init---");
    autoChooser.setDefaultOption("Drive Forward", auto_DriveForward);
    autoChooser.addOption("Do Nothing", auto_DoNothing);
    SmartDashboard.putData("Auto choices", autoChooser);

    ckController = new XboxController(0);
    ckDrive = new DriveTrain();
    ckPDP = new PowerDistributionPanel();
    ckBall = new BallShooter();
    ckArm = new HatchArm();
    ckPlatform = new Platform();

    ckCameraFront = CameraServer.getInstance().startAutomaticCapture();
    ckCameraFront.setResolution(160, 120);
    ckCameraFront.setFPS(15);

    ckCameraRear = CameraServer.getInstance().startAutomaticCapture();
    ckCameraRear.setResolution(160, 120);
    ckCameraRear.setFPS(15);

    // Vision
    // ckPixy = Pixy2.createInstance(new io.github.pseudoresonance.pixy2api.links.SPILink());
    // ckPixy.init(RMap.PixySPIPort);
    

    // Setup ARM Starting Position
    ckArm.startingPosition();
    // Hold Ramp Up
    ckPlatform.launchPlatform(PlatformPosition.Up);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * Setup Autonomous Variables, This is only called ONCE
   */
  @Override
  public void autonomousInit() {
    System.out.println("---Auto mode---");
    auto_Selected = autoChooser.getSelected();

    startTimer = System.currentTimeMillis();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    // currentTimer = System.currentTimeMillis();

    // switch (auto_Selected) {
    // case auto_DoNothing:
    //   // Do NOTHING!
    //   break;
    // case auto_DriveForward:
    // default:
    //   autoDriveForward();
    //   break;
    // }

    teleopPeriodic();

  }

  /*******************************************
   *** AUTO MODES ***
   *******************************************/

  public void autoDriveForward() {
    long autoTimer = currentTimer - startTimer;

    if (autoTimer < 3000) {
      ckDrive.driveStraight(0.2);; // drive straight forward at 50%
    } else {
      ckDrive.teleDriveCartesian(0, 0, 0);
    }

  }

  /*******************************************
   *** TELEOP ***
   *******************************************/

  /**
   * This function is called once entering test mode.
   */
  @Override
  public void teleopInit() {
    System.out.println("---Teleop mode---");
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {

    /*
    Current Control Layout for Robot 2019
    
    Fuction Name - Button/Button Combination
    Toggle Arm - Right Hand Bumper
    Release Fingers - Hold B Button
    Toggle Arm Turn - Left Bumper
    Ball In - Right Trigger
    Ball Out - Left Trigger
    End Game - Start & Select
    
    */

    // vision
    //ckPixy.getCCC().getBlocks(true, Pixy2CCC.CCC_SIG1, 2);

    // Trigger Ramp Variable Speed both trigger
    // Right trigger is positive therefore in, left trigger is negative therefore
    // reverse
    ckBall.setSpeed(ckController.getTriggerAxis(Hand.kRight) - ckController.getTriggerAxis(Hand.kLeft));

    // Right Bumper Toggle Arm up/down and rumble down
    if (ckController.getBumperPressed(Hand.kRight)) {
      ckArm.toggleArm(ckController);
    }

    // B Hold Fingers Out
    if (ckController.getBButtonPressed()) {
      ckArm.toggleFireFinger();
    } 

    // // B Hold Fingers Out
    // if (ckController.getBButton()) {
    //   ckArm.fireFinger(FingerPosition.Down);
    // } else {
    //   ckArm.fireFinger(FingerPosition.Up);
    // }

    // Left bumper turn arm
    if (ckController.getBumperPressed(Hand.kLeft)) {
      ckArm.toggleArmTurn();
    }

    // RAMP SUPER SAFE, START+SELECT
    if (ckController.getBackButton() && ckController.getStartButton()) {
      ckArm.firePlatformPosition(); // Make Sure arm is out of the way
      ckPlatform.launchPlatform(PlatformPosition.Down);
    }

    // Drive train (forward, rotation, strafe)
    ckDrive.teleDriveCartesian(-ckController.getY(GenericHID.Hand.kRight), ckController.getX(GenericHID.Hand.kRight),ckController.getX(GenericHID.Hand.kLeft));

    // // PixyCam sub-routine HOLD A BUTTON
    // if (ckController.getAButton()) {
    //   // cameraHatchDetector();
    // }

    // if (ckController.getStickButtonPressed(Hand.kLeft)) {
    //   //TODO: Use toggle on the stick to change between camera to intake bal (front) and shoot ball (back)
    // }

    if (ckController.getYButton()){
      ckArm.ledOn();
    }else{
      ckArm.ledOff();
    }
  }

  /*******************************************
   *** CAMERA STUFF ***
   *******************************************/
  // public void cameraHatchDetector() {
  //   // System.out.println(ckPixy.getCCC().getBlocks());

  //   double driveStrafe = 0;
  //   double driveForward = 0;
  //   double driveRotate = 0;


  //   ArrayList<Block> foundBlocks = ckPixy.getCCC().getBlocks();
  //   if (foundBlocks.size() == 2) {
  //     System.out.println("2 blocks found.");

  //     // get both blocks
  //     Block blockLeft = foundBlocks.get(0); // first block found
  //     Block blockRight = foundBlocks.get(1); // second block found

  //     // SWAP IF NEEDED
  //     if (blockLeft.getX() > blockRight.getX()) {
  //       Block blockTemp = blockLeft;
  //       blockLeft = blockRight;
  //       blockRight = blockTemp;
  //       blockTemp = null;
  //     }

  //     // get top x-coordinate of the block
  //     int xLeft = blockLeft.getX();
  //     int xRight = blockRight.getX();
  //     int blockXMid = (xLeft + xRight) / 2; // midpoint x-coordinates

      
  //     if ((blockXMid >= (RMap.cameraXMid - RMap.cameraXDeadZone))
  //         && (blockXMid <= (RMap.cameraXMid + RMap.cameraXDeadZone))) {
  //       // your close enough (reduce glitch)
  //       System.out.println("Strafe 0"); // based on pixy cam mounting location, actually driving forward/reverse.
  //       driveStrafe = 0;
  //     } else if (blockXMid >= (RMap.cameraXMid + RMap.cameraXDeadZone)) {
  //       // move right
  //       System.out.println("Strafe right");
  //       driveStrafe = RMap.cameraDriveStrafeRight;
  //     } else if (blockXMid <= (RMap.cameraXMid - RMap.cameraXDeadZone)) {
  //       // move left
  //       System.out.println("Strafe left");
  //       driveStrafe = RMap.cameraDriveStrafeLeft;
  //     }

  //     // width of 2 objects
  //     int blockWidth = (xRight - xLeft);

  //     if (blockWidth <= RMap.cameraBlockWidth) {
  //       // drive forward (blocks have a small width)
  //       System.out.println("Drive forwards"); // based on pixy cam mounting location, actually driving forward/reverse.
  //       driveForward = RMap.cameraDriveFast;
  //     } else if (blockWidth >= RMap.cameraBlockWidth) {
  //       // you are getting very close now
  //       System.out.println("Drive slowly");
  //       driveForward = RMap.cameraDriveSlow;
  //     }

  //     // find bigger object (w * h)
  //     int areaBlockLeft = blockLeft.getWidth() * blockLeft.getHeight();
  //     int areaBlockRight = blockRight.getWidth() * blockRight.getHeight();

  //     if (areaBlockLeft < areaBlockRight) {
  //       // turn left (drive left side faster than right side)
  //       System.out.println("Rotate left");
  //       driveRotate = RMap.cameraDriveTurnLeft;
  //     }
  //     if (areaBlockRight < areaBlockLeft) {
  //       // turn right (drive right side faster than left side)
  //       System.out.println("Rotate right");
  //       driveRotate = RMap.cameraDriveTurnRight;
  //     }
  //     // drive mecanum: pass fwd, strafe, rotate
  //   } else if (foundBlocks.size() == 1) {
  //     // System.out.println("1 block found.");
  //     Block block1 = foundBlocks.get(0);
  //     System.out.println("X:" + block1.getX() + "Y:" + block1.getY());
  //   } else {
  //     System.out.println("Not 1 or 2 blocks.");
  //   }

  //   //Drive the ROBOT
  //   ckDrive.teleDriveCartesian(driveForward, driveRotate, driveStrafe);
  // }

  @Override
  public void testInit() {
    System.out.println("---Test mode---");
  }

  /**
   * This function is called periodically during test mode.
   * 
   */
  @Override
  public void testPeriodic() {
    // System.out.print(ckPixy.getFPS());
    // System.out.print("--");
  }

}