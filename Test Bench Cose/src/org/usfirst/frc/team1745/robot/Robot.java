
package org.usfirst.frc.team1745.robot;


import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Joystick.ButtonType;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.Trigger.ButtonScheduler;
import edu.wpi.first.wpilibj.hal.CanTalonSRX;

/**
 * This is a demo program showing the use of the RobotDrive class.
 * The SampleRobot class is the base of a robot application that will automatically call your
 * Autonomous and OperatorControl methods at the right time as controlled by the switches on
 * the driver station or the field controls.
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 * WARNING: While it may look like a good choice to use for your code if you're inexperienced,
 * don't. Unless you know what you are doing, complex code will be much more difficult under
 * this system. Use IterativeRobot or Command-Based instead if you're new.
 */
public class Robot extends SampleRobot 
{
    Compressor myCompressor;
    CanTalonSRX myTalon;
    DoubleSolenoid mySolenoid;
    Joystick stick;

    public Robot() 
    {
       stick = new Joystick(0);
       myCompressor = new Compressor(2);
       myTalon = new CanTalonSRX(3);
       mySolenoid = new DoubleSolenoid(2,0,1);
    }

   
    public void autonomous() 
    {
      
    }

    
    public void operatorControl() {
        
        while (isOperatorControl() && isEnabled()) 
        {
          	// Map the Y axis of the joystick to the talon
        	myTalon.Set(stick.getY());
            
            // if the pressure is below switch preset turn on the compressor. else turn it off.
            if(myCompressor.getPressureSwitchValue())
            {
            	myCompressor.start();
            }
            else
            {
            	myCompressor.start();
            }
            // if the trigger is presses actuate the solenoid
            if(stick.getButton(ButtonType.kTrigger))
            {
            	mySolenoid.set(Value.kForward);
            }
            else
            {
            	mySolenoid.set(Value.kReverse);
            }
            	
            Timer.delay(0.005);		// wait for a motor update time
        }
    }

    /**
     * Runs during test mode
     */
    public void test() 
    {
    }
}
