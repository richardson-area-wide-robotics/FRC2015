
package org.usfirst.frc.team1745.robot;


import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ShapeMode;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.AxisCamera;

public class Robot extends SampleRobot 
{
    RobotDrive myRobot;
    
    CANTalon lift; 		//3
    CANTalon frontLeft; //4
    CANTalon backLeft; 	//5
    CANTalon frontRight;//6
    CANTalon backRight; //7
    
    DoubleSolenoid mySolenoid;	//2
    Compressor myCompressor;	//2
    
    Joystick stick;		//0
    Joystick gamepad;	//1
    
    SendableChooser dashBoard;
    Command autoChooser;
    
    boolean autoChoiceBin;
    boolean autoChoiceTote;
    boolean autoChoiceRobot;
    boolean autoChoiceNone;
    int autoChoice;  
    
    int session;
    Image frame;
    AxisCamera camera;
    
    Gyro gyro;
    
    boolean polarDrive;
    boolean cartesianDrive;

    JoystickButton armUpDrive;			//7
    JoystickButton armUpOp;				//7
    JoystickButton armDownDrive;		//8
    JoystickButton armDownOp;			//8
    JoystickButton clawDrive;			//10
    JoystickButton clawOp;				//10
    JoystickButton polarDriver;			//12
    JoystickButton polarOp;				//12
    JoystickButton cartesianDriver;		//11
    JoystickButton cartesianOp;			//11
    
    
    

    public Robot() 
    {
       
        lift = new CANTalon(3); // Lift
        frontLeft = new CANTalon(4); // Front Left
        backLeft = new CANTalon(5); // Back left    
        frontRight = new CANTalon(6); // Front Right
        backRight=new CANTalon(7); // Back Right

        mySolenoid = new DoubleSolenoid( 2, 0, 1);
        myCompressor = new Compressor(2);
        
        stick = new Joystick(0);
        gamepad = new Joystick(1);
       
        myRobot = new RobotDrive(frontLeft,backLeft,frontRight,backRight);
        myRobot.setExpiration(0.1);
        
        //unsure exactly how this works 
        /*driverStation.addObject("None", autoChoiceNone);
        driverStation.addObject("Bin", autoChoiceBin);
        driverStation.addObject("Tote", autoChoiceTote);
        driverStation.addObject("Robot", autoChoiceRobot);*/
        //or this
        SmartDashboard.putString("New Name", "None");
        SmartDashboard.putString("DB/Button 1", "Bin");
        SmartDashboard.putString("DB/Button 2", "Tote");
        SmartDashboard.putString("DB/Button 3", "Robot");
        //Same here
        autoChoiceBin=SmartDashboard.getBoolean("Bin");
        autoChoiceTote=SmartDashboard.getBoolean("Tote");
        autoChoiceRobot=SmartDashboard.getBoolean("Robot");
        autoChoiceNone=SmartDashboard.getBoolean("None");
        
        frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
        // open the camera at the IP address assigned. This is the IP address that the camera
        // can be accessed through the web interface.
        camera = new AxisCamera("10.1.91.100");
        
        //Need to get correct channel, 1 or 0
        gyro = new Gyro(0);
        
        //Start in Polar Drive Mode
        polarDrive=true;
        cartesianDrive=false;
        
        armUpDrive= new JoystickButton(stick,7);
        armUpOp = new JoystickButton(gamepad,7);
        armDownDrive= new JoystickButton(stick,8);
        armDownOp = new JoystickButton(gamepad,8);
        clawDrive= new JoystickButton(stick,10);
        clawOp = new JoystickButton(gamepad,10);
        polarDriver= new JoystickButton(stick,12);
        polarOp = new JoystickButton(gamepad,12);
        cartesianDriver= new JoystickButton(stick,11);
        cartesianOp = new JoystickButton(gamepad,11);
                
    }

    /**
     * Check Driver Station for which Autonomous mode to Run
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
            	System.out.println("Compressor off");
            }
            else
            {
            	myCompressor.start();
            	System.out.println("Compressor on");
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
    }

    /**
     * Runs the motors for Mecanum Drive and lift along with compressor and claw.
     */
    public void operatorControl() 
    {
        myRobot.setSafetyEnabled(true);
       /* while (isOperatorControl() && isEnabled()) 
        {*/
        	/**
             * grab an image from the camera, draw the circle, and provide it for the camera server
             * which will in turn send it to the dashboard.
             */
            NIVision.Rect rect = new NIVision.Rect(10, 10, 100, 100);

            while (isOperatorControl() && isEnabled()) {
            	
               //basic vision copied from example project
            	camera.getImage(frame);
                NIVision.imaqDrawShapeOnImage(frame, frame, rect,
                        DrawMode.DRAW_VALUE, ShapeMode.SHAPE_OVAL, 0.0f);

                CameraServer.getInstance().setImage(frame);
        	
        	
        	//lift up
        	if(armUpDrive.get()||armUpOp.get())
        	{
        		lift.set(.5);
        		System.out.println("Arm up");
        	}
        	else 
        		lift.set(0);
        	
        	//lift down
        	if(armDownDrive.get()||armDownOp.get())
        	{
        		lift.set(-.5);
        		System.out.println("Arm down");
        	}
        	else 
        		lift.set(0);
        	
        	
        	//Mecanum drive Switch with buttons 11 & 12
        	if (cartesianDriver.get()||cartesianOp.get())//Cartesian on
        	{
        		polarDrive=false;
        		System.out.println("Polar Drive Off");
        		cartesianDrive=true;
        		System.out.println("Cartision Drive On");
        	}
        	if (polarDriver.get()||polarOp.get())//Polar on
        	{
        		polarDrive=true;
        		System.out.println("Polar Drive On");
        		cartesianDrive=false;
        		System.out.println("Cartision Drive Off");
        	}

        	//Mecanum drive
        	if(polarDrive)
        		myRobot.mecanumDrive_Polar(stick.getX(), stick.getY(), stick.getTwist());
        	if(cartesianDrive)
        		myRobot.mecanumDrive_Cartesian(stick.getX(), stick.getY(), stick.getTwist(), gyro.getAngle());
        	
        	
        	//Turn on compressor if more air is needed
            if(myCompressor.getPressureSwitchValue())
            {
            	myCompressor.stop();
            	System.out.println("Compressor off");
            }
            else
            {
            	myCompressor.start();
            	System.out.println("Compressor on");
            }
            
            
            //Actuate Solenoid for claw
        	if(clawDrive.get()||clawOp.get())
        	{
            	mySolenoid.set(Value.kForward);
            	System.out.println("claw closed");
        	}
            else
            {
            	mySolenoid.set(Value.kReverse);
            	System.out.println("claw open");
            }

        	
        	//System.out.println("Talon Value: " + myTalon..toString());
        	System.out.println("Solenoid Switch Value: " + mySolenoid.get().toString());
        	System.out.println("Compessor Switch Value: " + myCompressor.getPressureSwitchValue());
        	
        	Timer.delay(0.005);		// wait for a motor update time
        }
       // }
    }
    
    

    /**
     * Runs during test mode
     */
    public void test() 
    {
    	System.out.println("Welcome to Test Mode!");
    }
}
