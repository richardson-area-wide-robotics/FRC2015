
package org.usfirst.frc.team1745.robot;


import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.hal.CanTalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
    RobotDrive myRobot;
    CANTalon lift; // Lift
    CANTalon frontLeft; //Front Left
    CANTalon backLeft; //Back Left
    CANTalon frontRight; //Front Right
    CANTalon backRight; //Back Right
    DoubleSolenoid mySolenoid;
    Joystick stick;
    Joystick gamepad;
    Compressor myCompressor;
    

    
    // The channel on the driver station that the joystick is connected to
    SendableChooser driverStation;
    boolean autoChoiceBin;
    boolean autoChoiceTote;
    boolean autoChoiceRobot;
    boolean autoChoiceNone;
    int autoChoice;
    

    

    public Robot() 
    {
       
        lift = new CANTalon(1); // Lift
        frontLeft = new CANTalon(2); // Front Left
        backLeft = new CANTalon(3); // Back left
        frontRight = new CANTalon(4); // Front Right
        backRight = new CANTalon(5); // Back Right
       
        mySolenoid = new DoubleSolenoid( 2, 0, 1);
        myCompressor = new Compressor(2);
        
        stick = new Joystick(0);
        gamepad = new Joystick(1);
       
        myRobot = new RobotDrive(frontLeft,backLeft,frontRight,backRight);
        myRobot.setExpiration(0.1);
        
        
        //unsure exactly how this works 
        driverStation.addObject("None", autoChoiceNone);
        driverStation.addObject("Bin", autoChoiceBin);
        driverStation.addObject("Tote", autoChoiceTote);
        driverStation.addObject("Robot", autoChoiceRobot);
        //or this
        autoChoiceBin=SmartDashboard.getBoolean("Bin");
        autoChoiceTote=SmartDashboard.getBoolean("Tote");
        autoChoiceRobot=SmartDashboard.getBoolean("Robot");
        autoChoiceNone=SmartDashboard.getBoolean("None");
        
        
    }

    /**
     * Drive left & right motors for 2 seconds then stop
     */
    public void autonomous() 
    {
    	myRobot.setSafetyEnabled(false);
    	
    	if(autoChoiceNone)
    		autoChoice=0;
    	if(autoChoiceBin)
    		autoChoice=1;
    	if(autoChoiceTote);
    		autoChoice=2;
		if(autoChoiceRobot)
			autoChoice=3;
		if(!(autoChoiceNone||autoChoiceBin||autoChoiceTote||autoChoiceRobot))
			autoChoice=3;
		
		while(isAutonomous()&&isEnabled())
		{
			//Turn on compressor if more air is needed
            if(myCompressor.getPressureSwitchValue())
            {
            	myCompressor.stop();
            }
            else
            {
            	myCompressor.start();
            }
            
            
            //switch between different auto modes via SmartDashboard
			switch(autoChoice)
			{
				case(0)://"None" autonomous selected
				{
					System.out.println("Robot will remain still.");
					break;
				}
				
				case(1)://"Bin" autonomous selected
				{
					System.out.println("Robot will pickup bin and move to auto zone.");
					break;
				}
				
				case(2)://"Tote" autonomous selected
				{
					System.out.println("Robot will pickup yellow tote and move to auto zone.");
					break;
				}
				
				case(3)://"Robot" autonomous selected or no choice made
				{
					System.out.println("Robot will move to auto zone.");
					break;
				}
			}
		}
        if(myCompressor.getPressureSwitchValue())
        {
        	myCompressor.stop();
        }
        else
        {
        	myCompressor.start();
        }
    }

    /**
     * Runs the motors with Mecanum Drive and lift.
     */
    public void operatorControl() 
    {
        myRobot.setSafetyEnabled(true);
        while (isOperatorControl() && isEnabled()) 
        {
           // spin motor for lift on joy stick throttle
        	//lift.set(stick.getThrottle()*0.1);
        	
        	//lift up
        	while(stick.getRawButton(7)||gamepad.getRawButton(1))
        		lift.set(.1);
        	lift.set(0);
        	
        	//lift down
        	while(stick.getRawButton(8)||gamepad.getRawButton(8))
        		lift.set(-.1);
        	lift.set(0);
        	
        	
        	//Mecanum drive
        	myRobot.mecanumDrive_Polar(stick.getX(), stick.getY(), stick.getTwist());
        	
        	//Turn on compressor if more air is needed
            if(myCompressor.getPressureSwitchValue())
            {
            	myCompressor.stop();
            }
            else
            {
            	myCompressor.start();
            }
            
            //Actuate Solenoid for claw
        	if(stick.getRawButton(10)||gamepad.getRawButton(10))
            	mySolenoid.set(Value.kForward);
            else
            	mySolenoid.set(Value.kReverse);
        		
        	
        	
        	//System.out.println("Tallon Value: " + myTalon..toString());
        	System.out.println("Solenoid Switch Value: " + mySolenoid.get().toString());
        	System.out.println("Compessor Switch Value: " + myCompressor.getPressureSwitchValue());
        	
        	Timer.delay(0.005);		// wait for a motor update time
        }
        
    }
    
    

    /**
     * Runs during test mode
     */
    public void test() 
    {
    	System.out.println("Welcome to Test Mode!");
    }
}
