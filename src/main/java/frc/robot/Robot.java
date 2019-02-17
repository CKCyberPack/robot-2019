/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.ArrayList;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
// import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import io.github.pseudoresonance.pixy2api.Pixy2;
import io.github.pseudoresonance.pixy2api.Pixy2CCC;
import io.github.pseudoresonance.pixy2api.Pixy2CCC.Block;
import frc.robot.HatchArm.ArmPosition;
import frc.robot.HatchArm.FingerPosition;
import frc.robot.Platform.PlatformPosition;
import java.util.Timer;
import edu.wpi.first.wpilibj.*;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  // private static final String kDefaultAuto = "Default";
  // private static final String kCustomAuto = "My Auto";
  // private String m_autoSelected;
  // private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private Pixy2 ckPixy;

  //robot components
  private XboxController ckController;
  private PowerDistributionPanel ckPDP;
  private DriveTrain ckDrive;
  private BallShooter ckBall;
  private HatchArm ckArm;
  private Platform ckPlatform;
  private Compressor ckCompressor;


  private long startTimer;
  private long currentTimer;
  // private Timer autoTimer;
  private int autoCase;


  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
   /* m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);*/

    ckController = new XboxController(0);
    ckDrive = new DriveTrain();
    ckPDP = new PowerDistributionPanel();
    ckBall = new BallShooter();
    ckArm = new HatchArm();
    ckPlatform = new Platform();
    ckCompressor = new Compressor();

    //vision
    ckPixy = Pixy2.createInstance(new io.github.pseudoresonance.pixy2api.links.SPILink());
    ckPixy.init(RMap.PixySPIPort);
    //System.out.println(ckPixy.getVersionInfo());
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable chooser
   * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
   * remove all of the chooser code and uncomment the getString line to get the
   * auto name from the text box below the Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional comparisons to the
   * switch structure below with additional strings. If using the SendableChooser
   * make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    System.out.println("---Auto mode---");
    //m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    //System.out.println("Auto selected: " + m_autoSelected);

    // autoTimer = new Timer();
    startTimer = System.currentTimeMillis();
    autoCase = 0;
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    currentTimer = System.currentTimeMillis();
    // switch (m_autoSelected) {
    // case kCustomAuto:
    //   // Put custom auto code here
    //   break;
    // case kDefaultAuto:
    // default:
    //   // Put default auto code here
    //   break;
    // }

    switch (autoCase)
    {
        case 0:
            ckDrive.teleDriveCartesian(0.5, 0, 0); //drive straight forward at 50%
            if ((currentTimer - startTimer) > 3000) {autoCase++;} //once time has passed move on
            break;
        case 1:
        default:
            ckDrive.teleDriveCartesian(0, 0, 0);
            cameraHatchDetector();
            break;
    }
}

  /**
  * This function is called once entering test mode.
  */
@Override
public void teleopInit() {
  System.out.println("---Teleop mode---");

  //TODO - Move this to robot init?
  //Arm Turn In
  ckArm.fireArm(ArmPosition.In);
  //Hold Ramp Up
  ckPlatform.launchPlatform(PlatformPosition.Up);
  // Start Arm UP
  ckArm.fireArm(ArmPosition.Up);
  // Start fingers in
  ckArm.fireFinger(FingerPosition.In);
}

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {

    //vision
    ckPixy.getCCC().getBlocks(true, Pixy2CCC.CCC_SIG1, 2);

   //Trigger Ramp Variable Speed both trigger
   //Right trigger is positive therefore in, left trigger is negative therefore reverse
   ckBall.setSpeed(ckController.getTriggerAxis(Hand.kLeft) - ckController.getTriggerAxis(Hand.kRight));

   //Right Bumper Toggle Arm up/down and rumble down
   if (ckController.getBumperPressed(Hand.kRight)){
     ckArm.toggleArm(ckController);
   }

   //Left Bumper Toggle Arm turn in/out
   //if (ckController.getBumperPressed(Hand.kLeft)){
    //ckArm.toggleArmTurn(ckController);
  //}

   //B Hold Fingers Out
   if (ckController.getBButton()){
     ckArm.fireFinger(FingerPosition.Out);
   }
   else{
     ckArm.fireFinger(FingerPosition.In);
   }

   //X turn arm
   if (ckController.getXButton()) {
     ckArm.fireArm(ArmPosition.Out);
   }
   else{
    ckArm.fireArm(ArmPosition.In);
  }

   //RAMP SUPER SAFE, START+SELECT
   if (ckController.getBackButton() && ckController.getStartButton()){ 
    ckArm.fireArm(ArmPosition.Out); //make sure arm can go down (turn it)
    ckArm.fireArm(ArmPosition.Down); //make sure arm is out of the way
    ckPlatform.launchPlatform(PlatformPosition.Down);
   }

    //Drive train             (forward, rotation, strafe)
    ckDrive.teleDriveCartesian(-ckController.getY(GenericHID.Hand.kRight), ckController.getX(GenericHID.Hand.kRight), ckController.getX(GenericHID.Hand.kLeft));

    //PixyCam sub-routine
    if (ckController.getAButton()){
      //cameraHatchDetector();
    }
  }
  

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
    //System.out.print(ckPixy.getFPS());
    //System.out.print("--");
  
  }

  public void cameraHatchDetector() {
    //System.out.println(ckPixy.getCCC().getBlocks());

    ArrayList<Block> foundBlocks = ckPixy.getCCC().getBlocks();
    if (foundBlocks.size() == 2) {
      System.out.println("2 blocks found.");
    
      //get both blocks
      Block blockLeft = foundBlocks.get(0); //first block found
      Block blockRight = foundBlocks.get(1); //second block found
          
      //SWAP IF NEEDED
      if (blockLeft.getX() > blockRight.getX()) {
        Block blockTemp = blockLeft;
        blockLeft = blockRight;
        blockRight = blockTemp;
        blockTemp = null;
      }
    
      //get top x-coordinate of the block
      int xLeft = blockLeft.getX();
      int xRight = blockRight.getX();
      int blockXMid = (xLeft + xRight)/2; //midpoint x-coordinates
    
      if ((blockXMid >= (RMap.cameraXMid - RMap.cameraXDeadZone)) && (blockXMid <= (RMap.cameraXMid + RMap.cameraXDeadZone))) {
        //your close enough (reduce glitch)
        System.out.println("Strafe 0"); //based on pixy cam mounting location, actually driving forward/reverse.
        ckDrive.teleDriveCartesian(0, 0, 0); 
      }
      else if (blockXMid >= (RMap.cameraXMid + RMap.cameraXDeadZone)) {
       // move right
        System.out.println("Strafe right");
        ckDrive.teleDriveCartesian(RMap.cameraDriveReverse, 0, 0); //drive reverse
      }
      else if(blockXMid <= (RMap.cameraXMid - RMap.cameraXDeadZone)){
        // move left
        System.out.println("Strafe left");
        ckDrive.teleDriveCartesian(RMap.cameraDriveForward, 0, 0); //drive forward
      }
          
      //width of 2 objects
      int blockWidth = (xRight - xLeft);
    
      if (blockWidth <= RMap.cameraBlockWidth){
        //drive forward (blocks have a small width) 
        System.out.println("Drive forwards"); //based on pixy cam mounting location, actually driving forward/reverse.
        ckDrive.teleDriveCartesian(0, 0, RMap.cameraDriveStarfeLeft); //strafe left
      }
      else if (blockWidth >= RMap.cameraBlockWidth){
        //you are getting very close now
        System.out.println("Drive slowly");
        ckDrive.teleDriveCartesian(0, 0, RMap.cameraDriveStrafeRight); //starfe right
      }
          
      //find bigger object (w * h)
      int areaBlockLeft = blockLeft.getWidth() * blockLeft.getHeight();
      int areaBlockRight = blockRight.getWidth() * blockRight.getHeight();
    
      if (areaBlockLeft < areaBlockRight) {
        //turn left (drive left side faster than right side)
        System.out.println("Rotate left");
        ckDrive.teleDriveCartesian(0, RMap.cameraDriveTurnLeft, 0); //rotate left
      }
      if (areaBlockRight < areaBlockLeft) {
        //turn right (drive right side faster than left side)
        System.out.println("Rotate right");
        ckDrive.teleDriveCartesian(0, RMap.cameraDriveTurnRight, 0); //rotate right
      }
      //drive mecanum: pass fwd, strafe, rotate
    }
    else if (foundBlocks.size() == 1)
    {
      //System.out.println("1 block found.");
      Block block1 = foundBlocks.get(0);
      System.out.println("X:" + block1.getX() + "Y:" + block1.getY());
    }
    else
    {
      System.out.println("Not 1 or 2 blocks.");
    }

  }
}