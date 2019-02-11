/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.*;
/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
  public static Joystick mainOI;

   /**
 * Initialize all the controller
 */
  public static void init(){
    mainOI = new Joystick(0);
  }

   /**
 * Cartesian X-Axis
 */
  public static double getDriveX(){
    double x = mainOI.getRawAxis(2)*0.5; //the *0.5 is important
    return x > Constant.joystickDeadZone || x < -Constant.joystickDeadZone ? x : 0;
  }

  /**
 * Cartesian Y-Axis
 */
  public static double getDriveY(){
    double y = -mainOI.getRawAxis(1)*0.8; //the *0.8 is important
    return y > Constant.joystickDeadZone || y < -Constant.joystickDeadZone ? y : 0;
  }
  
  public static boolean getDriveRefine(){
    //TODO Correct driveRefine button
    return mainOI.getRawButtonPressed(5); //L1
  }
  public static double getArm(){
    double y = mainOI.getRawAxis(3);
    return y > Constant.joystickDeadZone || y < -Constant.joystickDeadZone ? y : 0;
  }
  
  public static double getWrist(){
    double y = -mainOI.getRawAxis(4);
    return y > Constant.joystickDeadZone || y < -Constant.joystickDeadZone ? y : 0;
  }
  
  /**
   * <pre>
   * -2 = use PID to go to a lower level
   * -1 = manually going down
   *  0 = No action
   *  1 = manually going up
   *  2 = use PID to go to a Higher level
   * </pre>
   */
  public static int getLift() {
    //all button numbers are placeholders
    int output = 0;
    if(mainOI.getRawButtonPressed(2))output--;
    if(mainOI.getRawButtonPressed(4))output++;
    SmartDashboard.putNumber("Lift Controller Value", output);
    return output;
  }
}
