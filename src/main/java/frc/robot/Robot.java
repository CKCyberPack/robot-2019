/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import io.github.pseudoresonance.pixy2api.Pixy2;
import io.github.pseudoresonance.pixy2api.Pixy2CCC;
import io.github.pseudoresonance.pixy2api.Pixy2CCC.Block;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private Pixy2 ckPixy;

  //robot components
  private XboxController ckController;
  private PowerDistributionPanel ckPDP;
  private DriveTrain ckDrive;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
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
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
    case kCustomAuto:
      // Put custom auto code here
      break;
    case kDefaultAuto:
    default:
      // Put default auto code here
      break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
  }

   /**
   * This function is called once entering test mode.
   */
  @Override
  public void testInit() {
    System.out.println("---Test mode---");
    ckPixy = Pixy2.createInstance(new io.github.pseudoresonance.pixy2api.links.SPILink());
    ckPixy.init(RMap.PixySPIPort);

    System.out.println(ckPixy.getVersionInfo());
    
  }
  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    //System.out.print(ckPixy.getFPS());
    //System.out.print("--");
    
    ckPixy.getCCC().getBlocks(true, Pixy2CCC.CCC_SIG1, 2);
    
    //System.out.println(ckPixy.getCCC().getBlocks());
    

    ArrayList<Block> foundBlocks = ckPixy.getCCC().getBlocks();
    if (foundBlocks.size() == 2)
    {
      System.out.println("2 blocks found.");

      //get both blocks
      Block blockLeft = foundBlocks.get(0);
      Block blockRight = foundBlocks.get(0);
      int xLeft = blockLeft.getX();
      int xRight = blockRight.getX();
      
      //find center point on the picture (find center point ((x1 + x2)/2)
      int blockMidX = (xLeft + xRight)/2;

      if((blockMidX <= RMap.pixelLeft) && (blockMidX <= RMap.pixelRight)){
        // move left
        System.out.println("Drive left");
      }
      else if ((blockMidX >= RMap.pixelLeft) && (blockMidX >= RMap.pixelRight)) {
        // move right
        System.out.println("Drive right");
      }

      //width of 2 objects
      int width = (xRight - xLeft);
      int small = 50; //random value until we figure out what small is?
      
      //if width is small drive forward otherwise hit target
      if (width <= small){
        //drive forward
        System.out.println("Drive forwards");
      }
      else if (width >= small){
        //drive backwards
        System.out.println("Drive backwards");
      }
      
      //find bigger object (w * h)
      int widthLeft = blockLeft.getWidth();
      int heightLeft = blockLeft.getHeight();
      int areaLeft = widthLeft * heightLeft;

      int widthRight = blockRight.getWidth();
      int heightRight = blockRight.getHeight();
      int areaRight = widthRight * heightRight;

      if (areaLeft < areaRight) {
        //turn right (drive right side faster than left side)
        System.out.println("Turn right");
      }
      if (areaRight < areaLeft) {
        //turn left (drive left side faster than right side)
        System.out.println("Turn left");
      }

      //drive mecanum: pass fwd, side to side, rotate
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
    
    
  
    //ckDrive.teleDriveCartesian(-ckController.getY(GenericHID.Hand.kRight),ckController.getX(GenericHID.Hand.kRight), ckController.getX(GenericHID.Hand.kLeft));
  }
}
