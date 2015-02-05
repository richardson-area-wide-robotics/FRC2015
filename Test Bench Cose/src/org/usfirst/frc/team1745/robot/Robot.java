
package org.usfirst.frc.team1745.robot;


/*import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ShapeMode;*/

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
//import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
//import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
//import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import edu.wpi.first.wpilibj.vision.AxisCamera;
//import edu.wpi.first.wpilibj.vision.USBCamera;





import org.usfirst.frc.team1745.robot.P51RobotDefine;
import org.usfirst.frc.team1745.robot.P51Camera;

public class Robot extends SampleRobot 
{
    RobotDrive myRobot;
    
    CANTalon frontLeft; //3
    CANTalon frontRight;//4
    CANTalon backLeft; 	//5
    CANTalon backRight; //6
    CANTalon lift; 		//7
    
    int frontLeftEnc;
    int frontRightEnc;
    int backLeftEnc;
    int backRightEnc;
    
    DoubleSolenoid mySolenoid;	//2
    //Compressor myCompressor;	//2
    
    Joystick stick;		//0
    Joystick gamepad;	//1
    
    SendableChooser dashBoard;
    //Command autoChooser;
    LiveWindow vFeed;
    SmartDashboard dBoard;
    
    boolean autoChoiceBin;
    boolean autoChoiceTote;
    boolean autoChoiceRobot;
    boolean autoChoiceNone;
    int autoChoice;  
    
    //int session;
    //Image frame;
    //AxisCamera camera;
    //USBCamera usbCamera;
    //CameraServer cServer;
    P51Camera tServer;
    
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
    
    //Encoder frontLeftEnc;
    //Encoder frontRightEnc;
    //Encoder backLeftEnc;
    //Encoder backRightEnc;
    
    PrintWriter writer;

    public Robot()  
    {
       
        lift = new CANTalon(P51RobotDefine.winch_CANID); // Lift
        frontLeft = new CANTalon(P51RobotDefine.leftFrontMecanum_CANID); // Front Left
        backLeft = new CANTalon(P51RobotDefine.leftBackMecanum_CANID); // Back left    
        frontRight = new CANTalon(P51RobotDefine.rightFrontMecanum_CANID); // Front Right
        backRight=new CANTalon(P51RobotDefine.rightBackMecanum_CANID); // Back Right
        //myCompressor = new Compressor(P51RobotDefine.PCM_CANID);
        //mySolenoid = new DoubleSolenoid(P51RobotDefine.PCM_CANID, P51RobotDefine.clawSolenoidForward_PCMChan, P51RobotDefine.clawSolenoidBackwards_PCMChan);
        
        stick = new Joystick(P51RobotDefine.driver_USBJoyStick);
        gamepad = new Joystick(P51RobotDefine.operator_USBJoyStick);
       
        myRobot = new RobotDrive(frontLeft,backLeft,frontRight,backRight);
        myRobot.setInvertedMotor(MotorType.kFrontRight, true);
        myRobot.setInvertedMotor(MotorType.kRearRight, true);
        myRobot.setExpiration(0.1);
        
        //unsure exactly how this works 
        //dashBoard.addObject("None", autoChoiceNone);
        //dashBoard.addObject("Bin", autoChoiceBin);
        //dashBoard.addObject("Tote", autoChoiceTote);
        //dashBoard.addDefault("Robot", autoChoiceRobot);
        //SmartDashboard.putData("Auto-Mode Choice", dashBoard);
        //or this
        /*SmartDashboard.putString("nOne", "None");
        SmartDashboard.putString("bIn", "Bin");
        SmartDashboard.putString("tOte", "Tote");
        SmartDashboard.putString("rObot", "Robot");
        //Same here
        autoChoiceBin=SmartDashboard.getBoolean("bIn");
        autoChoiceTote=SmartDashboard.getBoolean("tOte");
        autoChoiceRobot=SmartDashboard.getBoolean("rObott");
        autoChoiceNone=SmartDashboard.getBoolean("nOne");*/
        
        //frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
        // open the camera at the IP address assigned. This is the IP address that the camera
        // can be accessed through the web interface.
        //camera = new AxisCamera("10.1.91.100");
        
        vFeed = new LiveWindow();
        LiveWindow.setEnabled(true);
        
        //usbCamera = new USBCamera();
        //cServer=CameraServer.getInstance();
        tServer= new P51Camera();
        
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
        
        //LiveWindow.addSensor("Camera", "Camera", tServer);
        //LiveWindow.setEnabled(true);
        //LiveWindow.run();
        
        /*xAxis=0;
        yAxis=0;
        twist=0;
        magnitude=0;
        angle=0;*/
        
        //frontLeftEnc = new Encoder(0,1,false,EncodingType.k2X);
        //frontRightEnc = new Encoder(0,1,true,EncodingType.k2X);
        //backLeftEnc = new Encoder(0,1,false,EncodingType.k2X);
        //backRightEnc = new Encoder(0,1,true,EncodingType.k2X);
        
        tServer.startAutomaticCapture();
        try {
			writer = new PrintWriter("P51Log.in");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			//@FIX COmpressor
			//Turn on compressor if more air is needed
           // if(myCompressor.getPressureSwitchValue())
            {
            	//@TODO Fix Compressor
            //	myCompressor.stop();
            	writer.println("Compressor off");
            }
            //else
            {
            	//@TODO Fix COmpressor
            	//myCompressor.start();
            	writer.println("Compressor on");
            }
            
            //switch between different auto modes via SmartDashboard
			switch(autoChoice)
			{
				case(0)://"None" autonomous selected
				{
					writer.println("Robot will remain still.");
					break;
				}
				
				case(1)://"Bin" autonomous selected
				{
					writer.println("Robot will pickup bin and move to auto zone.");
					break;
				}
				
				case(2)://"Tote" autonomous selected
				{
					writer.println("Robot will pickup yellow tote and move to auto zone.");
					break;
				}
				
				case(3)://"Robot" autonomous selected or no choice made
				{
					writer.println("Robot will move to auto zone.");
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
       writer.printf("before safety");
       myRobot.setSafetyEnabled(true);
       
  	   System.err.printf("before NI Vision");
       //NIVision.Rect rect = new NIVision.Rect(10, 10, 100, 100);
       writer.printf("after NI Vision");
       
       
       gyro.initGyro();
       gyro.startLiveWindowMode();
       
       //cServer.startAutomaticCapture(); 
       
       while (isOperatorControl() && isEnabled()) 
        {
        	//usbCamera.startCapture();
          	//usbCamera.getImage(frame);
          //NIVision.imaqDrawShapeOnImage(frame, frame, rect,DrawMode.DRAW_VALUE, ShapeMode.SHAPE_OVAL, 0.0f);
          //CameraServer.getInstance().setImage(frame);
          // writer.println("drawing on camera");
        	
    	   LiveWindow.run();
    	   LiveWindow.addSensor("Camera", 0, tServer);
    	   
    	   frontLeftEnc = frontLeft.getEncVelocity();
    	   frontRightEnc = frontRight.getEncVelocity();
    	   backLeftEnc = backLeft.getEncVelocity();
    	   backRightEnc = backRight.getEncVelocity();
    	   
        	writer.println(frontRightEnc);
        	//arm at rest  
        	if(!(armUpDrive.get()||armUpOp.get()||armDownDrive.get()||armDownOp.get()))
        	{
        		lift.set(0);
        	}
       		//lift up
       		if(armUpDrive.get()||armUpOp.get())        		
       		{
       			lift.set(P51RobotDefine.armUpSpeed);
       			writer.printf("Arm up");
       		}
       		//lift down
       		if(armDownDrive.get()||armDownOp.get())
       		{
       			lift.set(P51RobotDefine.armDownSpeed);
        		writer.printf("Arm down");
        	}
        	
       		
        	//Mecanum drive Switch with buttons 11 & 12
        	if (cartesianDriver.get()||cartesianOp.get())//Cartesian on
        	{
        		polarDrive=false;
        		writer.printf("Polar Drive Off");
        		cartesianDrive=true;
        		writer.printf("Cartesian Drive On");
        	}
        	if (polarDriver.get()||polarOp.get())//Polar on
        	{
        		polarDrive=true;
        		writer.printf("Polar Drive On");
        		cartesianDrive=false;
        		writer.printf("Cartesian Drive Off");
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
            //if(myCompressor.getPressureSwitchValue())
            {
            	//myCompressor.stop();
            	writer.printf("Compressor off");
            }
            //else
            {
            	//@TODO Fix Compressor
            	//myCompressor.start();
            	writer.printf("Compressor on");
            }
            
            
            //Actuate Solenoid for claw
        	if(clawDrive.get()||clawOp.get())
        	{
        		/*@TODO FIX Solenoid */
        		//mySolenoid.set(Value.kForward);
            	writer.printf("claw closed");
        	}
            else
            {/*@TODO FIX Solenoid */
            //	mySolenoid.set(Value.kReverse);
            	writer.printf("claw open");
            }
        	
        	Timer.delay(0.005);		// wait for a motor update time
        }
    }
    
    

    /**
     * Runs during test mode
     */
    public void test() 
    {
    	writer.printf("Welcome to Test Mode!");
    }
}
