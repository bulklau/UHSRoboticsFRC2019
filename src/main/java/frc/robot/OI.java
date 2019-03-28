/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.*;
import frc.robot.commands.commandgroups.goToSetHeight;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
  public static Joystick mainOI;
  public static Joystick subOI;
  public static JoystickButton cargoIntake; //L1
  public static JoystickButton cargoShoot; //R1
  public static JoystickButton hatchLoadingStation; //Left
  public static JoystickButton hatchLevel1; //Down
  public static JoystickButton hatchLevel2; //Right
  public static JoystickButton hatchLevel3; //Up
  public static JoystickButton groundPickup; //PS button
  private static int lastPOV = -1;



  public static void init(){
    mainOI = new Joystick(Constant.mainOI);
    subOI = new Joystick(Constant.subOI);

    //intake stufs
    cargoIntake = new JoystickButton(subOI, Constant.DualShock4.l1);
    cargoIntake.whileHeld(new CargoIntake());

    cargoShoot = new JoystickButton(subOI, Constant.DualShock4.r1);
    cargoShoot.whileHeld(new CargoShoot());

    hatchLoadingStation = new JoystickButton(subOI, Constant.DualShock4.left);
    hatchLoadingStation.whenPressed(new goToSetHeight(Constant.hatchLoading));

    hatchLevel1 = new JoystickButton(subOI, Constant.DualShock4.down);
    hatchLevel1.whenPressed(new goToSetHeight(Constant.hatch1));

    hatchLevel2 = new JoystickButton(subOI, Constant.DualShock4.right);
    hatchLevel2.whenPressed(new goToSetHeight(Constant.hatch2));

    hatchLevel3 = new JoystickButton(subOI, Constant.DualShock4.up);
    hatchLevel3.whenPressed(new goToSetHeight(Constant.hatch3));

    groundPickup = new JoystickButton(subOI, Constant.DualShock4.ps);
    groundPickup.whenPressed(new goToSetHeight(Constant.ground));

  }

  public static void check(){
    if(lastPOV!=subOI.getPOV()){
      switch(subOI.getPOV()){
        case 90:
        new goToSetHeight(Constant.cargoPickup);
        return;
        case 180:
        new goToSetHeight(Constant.cargo1);
        return;
        case 270:
        new goToSetHeight(Constant.cargo2);
        return;
        case 0:
        new goToSetHeight(Constant.cargo3);
        return;
      }
    }
    if(getWrist()!=0)new LiftManual();
  }
  /**
   * Cartesian X-Axis
   */
  public static double getDriveX() {
    return correctJoystick(-mainOI.getRawAxis(Constant.DualShock4.rightYAxis));
  }

  /**
   * Cartesian Y-Axis
   */
  public static double getDriveY() {
    return correctJoystick(-mainOI.getRawAxis(Constant.DualShock4.leftYAxis));
  }

  // TODO: make sure there's no "null pointer exception", and the button ids are
  // correct
  public static boolean getDriveLeft() {
    return mainOI.getRawButton(Constant.DualShock4.r1);
  }

  public static boolean getDriveRight() {
    return mainOI.getRawButton(Constant.DualShock4.l1);
  }

  public static double getArm() { // negative is going up
    double output = correctJoystick(0.85 * subOI.getRawAxis(Constant.DualShock4.rightYAxis));
    if (output == 0) {
      // 1 and 0 reserved for joystick
      if (subOI.getRawButton(1))
        output = 2; // hatch loading station (left btn)
      else if (subOI.getPOV() == 90)
        output = 3; // cargo loading station (right pov)
      else if (subOI.getRawButton(2))
        output = 4; // hatch level 1 (down btn)
      else if (subOI.getPOV() == 180)
        output = 5; // cargo level 1 (down pov)
      else if (subOI.getRawButton(3))
        output = 6; // hatch level 2 (right btn)
      else if (subOI.getPOV() == 270)
        output = 7; // cargo level 2 (left pov)
      else if (subOI.getRawButton(4))
        output = 8; // hatch level 3 (up btn)
      else if (subOI.getPOV() == 0)
        output = 9; // cargo level 3 (up pov)
      else if (subOI.getRawButton(13))
        output = 10; // ground pickup (ps button)
    }

    return output;
  }

  public static double getWrist() {
    double output = correctJoystick(subOI.getRawAxis(Constant.DualShock4.l2Axis));
    output -= correctJoystick(subOI.getRawAxis(Constant.DualShock4.r2Axis));
    output *= 0.3;
    if (output == 0) {
      // -1~1 is reserved for the joystick
      if (subOI.getRawButton(1))
        output = 2; // hatch loading station (left btn)
      else if (subOI.getPOV() == 90)
        output = 3; // cargo loading station(right pov)
      else if (subOI.getRawButton(2))
        output = 4; // hatch level 1 (down btn)
      else if (subOI.getPOV() == 180)
        output = 5; // cargo level 1 (down pov)
      else if (subOI.getRawButton(3))
        output = 6; // hatch level 2 (right btn)
      else if (subOI.getPOV() == 270)
        output = 7; // cargo level 2 (left pov)
      else if (subOI.getRawButton(4))
        output = 8; // hatch level 3 (up btn)
      else if (subOI.getPOV() == 0)
        output = 9; // cargo level 3 (up pov)
      else if (subOI.getRawButton(13))
        output = 10; // ground pickup (ps button)
    }
    return output;

  }

  public static double getLift() {
    return correctJoystick(-subOI.getRawAxis(Constant.DualShock4.leftYAxis)); // Left joystick
  }

  public static double correctJoystick(double input) {
    return input > Constant.joystickDeadZone || input < -Constant.joystickDeadZone ? input : 0;
  }
}
