
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
//import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.AxisCamera;
import edu.wpi.first.wpilibj.vision.USBCamera;

import org.usfirst.frc.team1745.robot.P51RobotDefine;

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
    //Command autoChooser;
    
    boolean autoChoiceBin;
    boolean autoChoiceTote;
    boolean autoChoiceRobot;
    boolean autoChoiceNone;
    int autoChoice;  
    
    int session;
    Image frame;
    AxisCamera camera;
    USBCamera usbCamera;
    
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
    
    /*double xAxis;
    double yAxis;
    double twist;
    double magnitude;
    double angle;*/
    
    
    

    public Robot() 
    {
       
        lift = new CANTalon(P51RobotDefine.winch_CANID); // Lift
        frontLeft = new CANTalon(P51RobotDefine.leftFrontMecanum_CANID); // Front Left
        backLeft = new CANTalon(P51RobotDefine.leftBackMecanum_CANID); // Back left    
        frontRight = new CANTalon(P51RobotDefine.rightFrontMecanum_CANID); // Front Right
        backRight=new CANTalon(P51RobotDefine.rightBackMecanum_CANID); // Back Right
        myCompressor = new Compressor(P51RobotDefine.PCM_CANID);
       // mySolenoid = new DoubleSolenoid(P51RobotDefine.PCM_CANID, P51RobotDefine.clawSolenoidForward_PCMChan, P51RobotDefine.clawSolenoidBackwards_PCMChan);
       
        
        stick = new Joystick(P51RobotDefine.driver_USBJoyStick);
        gamepad = new Joystick(P51RobotDefine.operator_USBJoyStick);
       
        myRobot = new RobotDrive(frontLeft,backLeft,frontRight,backRight);
        myRobot.setExpiration(0.1);
        
        //unsure exactly how this works 
        /*driverStation.addObject("None", autoChoiceNone);
        driverStation.addObject("Bin", autoChoiceBin);
        driverStation.addObject("Tote", autoChoiceTote);
        driverStation.addObject("Robot", autoChoiceRobot);/
        //or this
        SmartDashboard.putString("New Name", "None");
        SmartDashboard.putString("DB/Button 1", "Bin");
        SmartDashboard.putString("DB/Button 2", "Tote");
        SmartDashboard.putString("DB/Button 3", "Robot");
        //Same here
        autoChoiceBin=SmartDashboard.getBoolean("Bin");
        autoChoiceTote=SmartDashboard.getBoolean("Tote");
        autoChoiceRobot=SmartDashboard.getBoolean("Robot");
        autoChoiceNone=SmartDashboard.getBoolean("None");*/
        
        frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
        // open the camera at the IP address assigned. This is the IP address that the camera
        // can be accessed through the web interface.
        camera = new AxisCamera("10.1.91.100");
        
        usbCamera = new USBCamera("Camera");
        
        //Need to get correct channel, 1 or 0
        gyro = new Gyro(P51RobotDefine.gyro_ANAChan);
        
        //Start in Polar Drive Mode
        polarDrive=true;
        cartesianDrive=false;
        
        armUpDrive= new JoystickButton(stick,P51RobotDefine.armUpButton_Driver);
        armUpOp = new JoystickButton(gamepad,P51RobotDefine.armUpButton_Operator);
        armDownDrive= new JoystickButton(stick,P51RobotDefine.armDownButton_Driver);
        armDownOp = new JoystickButton(gamepad,P51RobotDefine.armDownButton_Operator);
        clawDrive= new JoystickButton(stick,P51RobotDefine.clawControl_Driver);
        clawOp = new JoystickButton(gamepad,P51RobotDefine.clawControl_Operator);
        polarDriver= new JoystickButton(stick,P51RobotDefine.mecanumModeToPolar_Driver);
        polarOp = new JoystickButton(gamepad,P51RobotDefine.mecanumModeToPolar_Operator);
        cartesianDriver= new JoystickButton(stick,P51RobotDefine.mecanumModeToCartesian_Driver);
        cartesianOp = new JoystickButton(gamepad,P51RobotDefine.mecanumModeToCartesian_Operator);
                
        /*xAxis=0;
        yAxis=0;
        twist=0;
        magnitude=0;
        angle=0;*/
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
			/*@FIX COmpressor
			//Turn on compressor if more air is needed
            if(myCompressor.getPressureSwitchValue())
            {
            	//@TODO Fix Compressor
            	//myCompressor.stop();
            	System.out.println("Compressor off");
            }
            else
            {
            	//@TODO Fix COmpressor
            	//myCompressor.start();
            	System.out.println("Compressor on");
            }
            */
            
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
    	System.out.printf("before safety");
       myRobot.setSafetyEnabled(true);
       /**
       * grab an image from the camera, draw the circle, and provide it for the camera server
       * which will in turn send it to the dashboard.
       */
  	   System.err.printf("before NI Vision");
       NIVision.Rect rect = new NIVision.Rect(10, 10, 100, 100);
       System.out.printf("after NI Vision");
        while (isOperatorControl() && isEnabled()) 
        {
           	usbCamera.getImage(frame);
          //NIVision.imaqDrawShapeOnImage(frame, frame, rect,DrawMode.DRAW_VALUE, ShapeMode.SHAPE_OVAL, 0.0f);
          //CameraServer.getInstance().setImage(frame);
          // System.out.println("drawing on camera");
        	
        	
        	//arm at rest  
        	if(!(armUpDrive.get()||armUpOp.get()||armDownDrive.get()||armDownOp.get()))
        	{
        		lift.set(0);
        	}
       		//lift up
       		if(armUpDrive.get()||armUpOp.get())        		
       		{
       			lift.set(P51RobotDefine.armUpSpeed);
       			System.out.printf("Arm up");
       		}
       		//lift down
       		if(armDownDrive.get()||armDownOp.get())
       		{
       			lift.set(P51RobotDefine.armDownSpeed);
        		System.out.printf("Arm down");
        	}
        	
       		
        	//Mecanum drive Switch with buttons 11 & 12
        	if (cartesianDriver.get()||cartesianOp.get())//Cartesian on
        	{
        		polarDrive=false;
        		System.out.printf("Polar Drive Off");
        		cartesianDrive=true;
        		System.out.printf("Cartision Drive On");
        	}
        	if (polarDriver.get()||polarOp.get())//Polar on
        	{
        		polarDrive=true;
        		System.out.printf("Polar Drive On");
        		cartesianDrive=false;
        		System.out.printf("Cartesian Drive Off");
        	}

        	
        	//Mecanum drive
        	if(stick.getX()!=0||stick.getY()!=0||stick.getTwist()!=0)
        	{
        	
        		/*if(stick.getX()>.05)
        			xAxis=Math.pow((stick.getX()-.05)/(.95),3);
        		if(stick.getX()<-.05)
        			xAxis=Math.pow((stick.getX()+.05)/(.95),3);
        	
        		if(stick.getY()>.05)
        			yAxis=Math.pow((stick.getY()-.05)/(.95),3);
        		if(stick.getY()<-.05)
        			yAxis=Math.pow((stick.getY()+.05)/(.95),3);
        	
        		if(stick.getTwist()>.05)
        			twist=Math.pow((stick.getTwist()-.05)/(.95),3);
        		if(stick.getTwist()<-.05)
        			twist=Math.pow((stick.getTwist()+.05)/(.95),3);*/
        		
        		
        		if(polarDrive)
        		{
        			//magnitude = Math.sqrt(Math.pow(xAxis,2)+Math.pow(yAxis,2));
        			//angle = Math.atan2(yAxis,xAxis);
        			myRobot.mecanumDrive_Polar(RobotDriveMath.polarMagnitude(stick.getX(),stick.getY()),
        					RobotDriveMath.polarAngle(stick.getX(),stick.getY()),
        					RobotDriveMath.twistWithDeadband(stick.getTwist()));
        		}
        		if(cartesianDrive)
        			myRobot.mecanumDrive_Cartesian(RobotDriveMath.xWithDeadband(stick.getX()),
        					RobotDriveMath.yWithDeadband(stick.getY()),
        					RobotDriveMath.twistWithDeadband(stick.getTwist()), gyro.getAngle());
        	}
        	
        	//Turn on compressor if more air is needed
            if(myCompressor.getPressureSwitchValue())
            {
            	myCompressor.stop();
            	System.out.printf("Compressor off");
            }
            else
            {
            	//@TODO Fix Compressor
            	myCompressor.start();
            	System.out.printf("Compressor on");
            }
            
            
            //Actuate Solenoid for claw
        	if(clawDrive.get()||clawOp.get())
        	{
        		/*@TODO FIX Solenoid */
        		//mySolenoid.set(Value.kForward);
            	System.out.printf("claw closed");
        	}
            else
            {/*@TODO FIX Solenoid */
            //	mySolenoid.set(Value.kReverse);
            	System.out.printf("claw open");
            }
        	
        	Timer.delay(0.005);		// wait for a motor update time
        }
    }
    
    

    /**
     * Runs during test mode
     */
    public void test() 
    {
    	System.out.printf("Welcome to Test Mode!");
    }
}
