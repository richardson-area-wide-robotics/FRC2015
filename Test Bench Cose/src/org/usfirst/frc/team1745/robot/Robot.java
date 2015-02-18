
package org.usfirst.frc.team1745.robot;


/*import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ShapeMode;*/

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.Vector;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.PIDController;
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
//import edu.wpi.first.wpilibj.vision.USBCamera;
import edu.wpi.first.wpilibj.vision.AxisCamera;
import org.usfirst.frc.team1745.robot.P51RobotDefine;
import org.usfirst.frc.team1745.robot.P51Camera;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.IMAQdxBufferNumberMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageType;

public class Robot extends SampleRobot 
{
    final RobotDrive myRobot;
    
    final CANTalon frontLeft; //3
    final CANTalon frontRight;//4
    final CANTalon backLeft; 	//5
    final CANTalon backRight; //6
    final CANTalon liftMain; 	//7
    final CANTalon liftSlave;	//8
    
    /*double frontLeftEnc;
    double frontRightEnc;
    double backLeftEnc;
    double backRightEnc;*/
    
    final DoubleSolenoid mySolenoid;	//2
    final Compressor myCompressor;	//2
    
    final Joystick stick;		//0
    final Joystick gamepad;	//1
    
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
    AxisCamera aCamera;
    //USBCamera usbCamera;
    //CameraServer cServer;
    P51Camera tServer;
    
    static Gyro gyro;
    
    static boolean polarDrive;
    static boolean cartesianDrive;

    JoystickButton armUpDrive;			//7
    JoystickButton armUpOp;				//7
    
    JoystickButton armDownDrive;		//8
    JoystickButton armDownOp;			//8
    
    JoystickButton clawDrive;			//10
    JoystickButton clawTrigger;			//1
    JoystickButton clawOp;				//10
    
    JoystickButton polarDriver;			//11
    JoystickButton polarOp;				//11
    
    JoystickButton cartesianDriver;		//12
    JoystickButton cartesianOp;			//12
    
    JoystickButton deadbandInc;			//3
    JoystickButton deadbandDec;			//4
    
    JoystickButton PIDControlOn; 		//5
    JoystickButton PIDControlOff;
    
    
    Toggle deadbandUp;
    Toggle deadbandDown;
    Toggle PIDToggle;
    
    /*PIDController frontLeftPID;
    PIDController frontRightPID;
    PIDController backLeftPID;
    PIDController backRightPID;*/

    double frontLeftSpeed;
    double frontRightSpeed;
    double backLeftSpeed;
    double backRightSpeed;
    
    /*double xAxis;
    double yAxis;
    double twist;
    double magnitude;
    double angle;*/
    
    //Encoder frontLeftEnc;
    //Encoder frontRightEnc;
    //Encoder backLeftEnc;
    //Encoder backRightEnc;
    
	//Images
	Image frame;
	Image binaryFrame;
	int imaqError;
	//CameraServer server;

	//Constants
	NIVision.Range TOTE_HUE_RANGE = new NIVision.Range(24, 49);	//Default hue range for yellow tote
	NIVision.Range TOTE_SAT_RANGE = new NIVision.Range(67, 255);	//Default saturation range for yellow tote
	NIVision.Range TOTE_VAL_RANGE = new NIVision.Range(49, 255);	//Default value range for yellow tote
	double AREA_MINIMUM = 0.5; //Default Area minimum for particle as a percentage of total image area
	double LONG_RATIO = 2.22; //Tote long side = 26.9 / Tote height = 12.1 = 2.22
	double SHORT_RATIO = 1.4; //Tote short side = 16.9 / Tote height = 12.1 = 1.4
	double SCORE_MIN = 75.0;  //Minimum score to be considered a tote
	double VIEW_ANGLE = 49.4; //View angle fo camera, set to Axis m1011 by default, 64 for m1013, 51.7 for 206, 52 for HD3000 square, 60 for HD3000 640x480
	NIVision.ParticleFilterCriteria2 criteria[] = new NIVision.ParticleFilterCriteria2[1];
	NIVision.ParticleFilterOptions2 filterOptions = new NIVision.ParticleFilterOptions2(0,0,1,1);
	Scores scores = new Scores();
	
	String P;
	String I;
	String D;
	boolean PIDNeedsInit;
	boolean runPID;
	boolean PIDDeacivate;
	
    public Robot()  
    {
        frontLeft = new CANTalon(P51RobotDefine.leftFrontMecanum_CANID); // Front Left
        backLeft = new CANTalon(P51RobotDefine.leftBackMecanum_CANID); // Back left    
        frontRight = new CANTalon(P51RobotDefine.rightFrontMecanum_CANID); // Front Right
        backRight=new CANTalon(P51RobotDefine.rightBackMecanum_CANID); // Back Right
        liftMain = new CANTalon(P51RobotDefine.winch_CANID); // Lift
        liftSlave =  new CANTalon(P51RobotDefine.winchSlave_CANID); // Lift Slave
        
        myCompressor = new Compressor(P51RobotDefine.PCM_CANID);
        mySolenoid = new DoubleSolenoid(P51RobotDefine.PCM_CANID, P51RobotDefine.clawSolenoidForward_PCMChan, P51RobotDefine.clawSolenoidBackwards_PCMChan);
        
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
        SmartDashboard.putString("rObot", "Robot");*/

        
        /*autoChoiceBin=SmartDashboard.getBoolean("Bin");
        autoChoiceTote=SmartDashboard.getBoolean("Tote");
        autoChoiceRobot=SmartDashboard.getBoolean("Robot");
        autoChoiceNone=SmartDashboard.getBoolean("None");*/
        
        //frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
        // open the camera at the IP address assigned. This is the IP address that the camera
        // can be accessed through the web interface.
        //camera = new AxisCamera("10.1.91.21");
        
        //vFeed = new LiveWindow();
        //LiveWindow.setEnabled(true);
        
        //usbCamera = new USBCamera();
        //cServer=CameraServer.getInstance();
        tServer= new P51Camera();
        aCamera = new AxisCamera("10.17.45.21");
       
        //Need to get correct channel, 1 or 0
        gyro = new Gyro(P51RobotDefine.gyro_ANAChan);
        
        //Start in Polar Drive Mode
        polarDrive=true;
        cartesianDrive=false;
        
        armUpDrive= new JoystickButton(stick, P51RobotDefine.armUpButton_Driver);					//7
        armUpOp = new JoystickButton(gamepad, P51RobotDefine.armUpButton_Operator);					//7
        
        armDownDrive= new JoystickButton(stick, P51RobotDefine.armDownButton_Driver);				//8
        armDownOp = new JoystickButton(gamepad, P51RobotDefine.armDownButton_Operator);				//8
        
        clawDrive= new JoystickButton(stick, P51RobotDefine.clawControl_Driver);					//10
        clawTrigger = new JoystickButton(stick, P51RobotDefine.clawControl_Trigger);				//1
        clawOp = new JoystickButton(gamepad, P51RobotDefine.clawControl_Operator);					//10
        
        polarDriver= new JoystickButton(stick, P51RobotDefine.mecanumModeToPolar_Driver);			//11
        polarOp = new JoystickButton(gamepad, P51RobotDefine.mecanumModeToPolar_Operator);			//11
        
        cartesianDriver= new JoystickButton(stick, P51RobotDefine.mecanumModeToCartesian_Driver);	//12
        cartesianOp = new JoystickButton(gamepad, P51RobotDefine.mecanumModeToCartesian_Operator);	//12
        
        deadbandInc = new JoystickButton(stick, P51RobotDefine.deadbandExpIncrement);				//3
        deadbandDec = new JoystickButton(stick, P51RobotDefine.deadbandExpDecrement);				//4
        
        PIDControlOn = new JoystickButton(stick, P51RobotDefine.PIDControlerOn);					//5
        PIDControlOff = new JoystickButton(stick, P51RobotDefine.PIDControlerOff);					//6

        
        deadbandUp = new Toggle(false);
        deadbandDown = new Toggle(false);
        PIDToggle = new Toggle(false);
        
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
        
        //tServer.startAutomaticCapture();
       
    	/*frontLeft.changeControlMode(ControlMode.valueOf(15));	//Change to 2 to run PID on start
    	frontRight.changeControlMode(ControlMode.valueOf(15));	//Change to 2 to run PID on start
    	backLeft.changeControlMode(ControlMode.valueOf(15));	//Change to 2 to run PID on start
    	backRight.changeControlMode(ControlMode.valueOf(15));	//Change to 2 to run PID on start
    	liftMain.changeControlMode(ControlMode.valueOf(15));	//***Change to 1 to run PID on start***/
    	liftSlave.changeControlMode(ControlMode.valueOf(5));	
    	
    	liftSlave.set(P51RobotDefine.winch_CANID);
    	
    	P = "P";
    	I = "I";
    	D = "D";
    	
    	PIDNeedsInit = true;
    	runPID = false;			//Change to true to run PID on Start
    	PIDDeacivate = false;
    }
    
		public class ParticleReport implements Comparator<ParticleReport>, Comparable<ParticleReport>
		{
			double PercentAreaToImageArea;
			double Area;
			double ConvexHullArea;
			double BoundingRectLeft;
			double BoundingRectTop;
			double BoundingRectRight;
			double BoundingRectBottom;
			
			public int compareTo(ParticleReport r)
			{
				return (int)(r.Area - this.Area);
			}
			
			public int compare(ParticleReport r1, ParticleReport r2)
			{
				return (int)(r1.Area - r2.Area);
			}
		};

		//Structure to represent the scores for the various tests used for target identification
		public class Scores {
			double Trapezoid;
			double LongAspect;
			double ShortAspect;
			double AreaToConvexHullArea;
		};

    
		public void robotInit() {
		    // create images
			frame = NIVision.imaqCreateImage(ImageType.IMAGE_RGB, 0);
			binaryFrame = NIVision.imaqCreateImage(ImageType.IMAGE_U8, 0);
			criteria[0] = new NIVision.ParticleFilterCriteria2(NIVision.MeasurementType.MT_AREA_BY_IMAGE_AREA, AREA_MINIMUM, 100.0, 0, 0);
			//server = CameraServer.getInstance();
			
			//Put default values to SmartDashboard so fields will appear
			SmartDashboard.putNumber("Tote hue min", TOTE_HUE_RANGE.minValue);
			SmartDashboard.putNumber("Tote hue max", TOTE_HUE_RANGE.maxValue);
			SmartDashboard.putNumber("Tote sat min", TOTE_SAT_RANGE.minValue);
			SmartDashboard.putNumber("Tote sat max", TOTE_SAT_RANGE.maxValue);
			SmartDashboard.putNumber("Tote val min", TOTE_VAL_RANGE.minValue);
			SmartDashboard.putNumber("Tote val max", TOTE_VAL_RANGE.maxValue);
			SmartDashboard.putNumber("Area min %", AREA_MINIMUM);
			
			SmartDashboard.putNumber("P Value", 1);
			SmartDashboard.putNumber("I Value", 1);
			SmartDashboard.putNumber("D Value", 1);
			SmartDashboard.putBoolean("PID", true);
			
			SmartDashboard.putBoolean("Polar drive", polarDrive);
    		SmartDashboard.putBoolean("Cartesian drive", cartesianDrive);
			
			SmartDashboard.putNumber("Front Left Speed",frontLeftSpeed);
			SmartDashboard.putNumber("Front Right Speed", frontRightSpeed);
			SmartDashboard.putNumber("Back Left Speed", backLeftSpeed);
			SmartDashboard.putNumber("Back Right Speed", backRightSpeed);
		    
			SmartDashboard.putBoolean("Polar Drive",polarDrive);
			SmartDashboard.putBoolean("Cartesian Drive",cartesianDrive);
			
	        SmartDashboard.putBoolean("Bin",false);
	        SmartDashboard.putBoolean("Tote",false);
	        SmartDashboard.putBoolean("Robot",false);
	        SmartDashboard.putBoolean("None",false);
	        
	        SmartDashboard.putNumber("Deadband Exponent", P51RobotDefine.deadbandExponent);
	        
	        SmartDashboard.putBoolean("Compressor On", myCompressor.getPressureSwitchValue());
			
			//SmartDashboard.putString(P, Double.toString(SmartDashboard.getNumber("P Value")));
			//SmartDashboard.putString(I, Double.toString(SmartDashboard.getNumber("I Value")));
			//SmartDashboard.putString(D, Double.toString(SmartDashboard.getNumber("D Value")));
			System.out.println("before Gyro");
			gyro.initGyro();
			System.out.println("after Gyro");
			
			//@TODO find id number
			NIVision.IMAQdxStartAcquisition(0);
		}
        
    

    /**
     * Check Driver Station for which Autonomous mode to Run
     */	
    public void autonomous() 
    {
    	myRobot.setSafetyEnabled(false);
    	
		while(isAutonomous()&&isEnabled())
		{
		//read file in from disk. For this example to run you need to copy image20.jpg from the SampleImages folder to the
		//directory shown below using FTP or SFTP: http://wpilib.screenstepslive.com/s/4485/m/24166/l/282299-roborio-ftp
		//NIVision.imaqReadFile(frame, "/home/lvuser/SampleImages/image20.jpg");;
		NIVision.IMAQdxGetImage(0, frame, IMAQdxBufferNumberMode.fromValue(2), 2);
		
		//Update threshold values from SmartDashboard. For performance reasons it is recommended to remove this after calibration is finished.
		TOTE_HUE_RANGE.minValue = (int)SmartDashboard.getNumber("Tote hue min", TOTE_HUE_RANGE.minValue);
		TOTE_HUE_RANGE.maxValue = (int)SmartDashboard.getNumber("Tote hue max", TOTE_HUE_RANGE.maxValue);
		TOTE_SAT_RANGE.minValue = (int)SmartDashboard.getNumber("Tote sat min", TOTE_SAT_RANGE.minValue);
		TOTE_SAT_RANGE.maxValue = (int)SmartDashboard.getNumber("Tote sat max", TOTE_SAT_RANGE.maxValue);
		TOTE_VAL_RANGE.minValue = (int)SmartDashboard.getNumber("Tote val min", TOTE_VAL_RANGE.minValue);
		TOTE_VAL_RANGE.maxValue = (int)SmartDashboard.getNumber("Tote val max", TOTE_VAL_RANGE.maxValue);

		//Threshold the image looking for yellow (tote color)
		NIVision.imaqColorThreshold(binaryFrame, frame, 255, NIVision.ColorMode.HSV, TOTE_HUE_RANGE, TOTE_SAT_RANGE, TOTE_VAL_RANGE);

		//Send particle count to dashboard
		int numParticles = NIVision.imaqCountParticles(binaryFrame, 1);
		SmartDashboard.putNumber("Masked particles", numParticles);

		//Send masked image to dashboard to assist in tweaking mask.
		//CameraServer.getInstance().setImage(binaryFrame);

		//filter out small particles
		float areaMin = (float)SmartDashboard.getNumber("Area min %", AREA_MINIMUM);
		criteria[0].lower = areaMin;
		imaqError = NIVision.imaqParticleFilter4(binaryFrame, binaryFrame, criteria, filterOptions, null);

		//Send particle count after filtering to dashboard
		numParticles = NIVision.imaqCountParticles(binaryFrame, 1);
		SmartDashboard.putNumber("Filtered particles", numParticles);

		if(numParticles > 0)
		{
			//Measure particles and sort by particle size
			Vector<ParticleReport> particles = new Vector<ParticleReport>();
			for(int particleIndex = 0; particleIndex < numParticles; particleIndex++)
			{
				ParticleReport par = new ParticleReport();
				par.PercentAreaToImageArea = NIVision.imaqMeasureParticle(binaryFrame, particleIndex, 0, NIVision.MeasurementType.MT_AREA_BY_IMAGE_AREA);
				par.Area = NIVision.imaqMeasureParticle(binaryFrame, particleIndex, 0, NIVision.MeasurementType.MT_AREA);
				par.ConvexHullArea = NIVision.imaqMeasureParticle(binaryFrame, particleIndex, 0, NIVision.MeasurementType.MT_CONVEX_HULL_AREA);
				par.BoundingRectTop = NIVision.imaqMeasureParticle(binaryFrame, particleIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_TOP);
				par.BoundingRectLeft = NIVision.imaqMeasureParticle(binaryFrame, particleIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_LEFT);
				par.BoundingRectBottom = NIVision.imaqMeasureParticle(binaryFrame, particleIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_BOTTOM);
				par.BoundingRectRight = NIVision.imaqMeasureParticle(binaryFrame, particleIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_RIGHT);
				particles.add(par);
			}
			particles.sort(null);

			//This example only scores the largest particle. Extending to score all particles and choosing the desired one is left as an exercise
			//for the reader. Note that the long and short side scores expect a single tote and will not work for a stack of 2 or more totes.
			//Modification of the code to accommodate 2 or more stacked totes is left as an exercise for the reader.
			scores.Trapezoid = TrapezoidScore(particles.elementAt(0));
			SmartDashboard.putNumber("Trapezoid", scores.Trapezoid);
			scores.LongAspect = LongSideScore(particles.elementAt(0));
			SmartDashboard.putNumber("Long Aspect", scores.LongAspect);
			scores.ShortAspect = ShortSideScore(particles.elementAt(0));
			SmartDashboard.putNumber("Short Aspect", scores.ShortAspect);
			scores.AreaToConvexHullArea = ConvexHullAreaScore(particles.elementAt(0));
			SmartDashboard.putNumber("Convex Hull Area", scores.AreaToConvexHullArea);
			boolean isTote = scores.Trapezoid > SCORE_MIN && (scores.LongAspect > SCORE_MIN || scores.ShortAspect > SCORE_MIN) && scores.AreaToConvexHullArea > SCORE_MIN;
			boolean isLong = scores.LongAspect > scores.ShortAspect;

			//Send distance and tote status to dashboard. The bounding rect, particularly the horizontal center (left - right) may be useful for rotating/driving towards a tote
			SmartDashboard.putBoolean("IsTote", isTote);
			SmartDashboard.putNumber("Distance", computeDistance(binaryFrame, particles.elementAt(0), isLong));
		} else {
			SmartDashboard.putBoolean("IsTote", false);
		}

		Timer.delay(0.005);				// wait for a motor update time
    	
		
        autoChoiceBin=SmartDashboard.getBoolean("Bin");
        autoChoiceTote=SmartDashboard.getBoolean("Tote");
        autoChoiceRobot=SmartDashboard.getBoolean("Robot");
        autoChoiceNone=SmartDashboard.getBoolean("None");
    	
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
		

			//@FIX COmpressor
			//Turn on compressor if more air is needed
           // if(myCompressor.getPressureSwitchValue())
            {
            	//@TODO Fix Compressor
            //	myCompressor.stop();
            	System.out.println("Compressor off");
            }
            //else
            {
            	//@TODO Fix COmpressor
            	//myCompressor.start();
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
       System.out.println("before safety");
       myRobot.setSafetyEnabled(true);
       System.out.println("after safety");
       gyro.startLiveWindowMode();
       //cServer.startAutomaticCapture(); 
       while (isOperatorControl() && isEnabled()) 
        {
    	   
    	   System.out.println("Teleopenabled");
    	   System.out.println("in While Loop");	
           //CameraServer.getInstance().setImage(frame);
        	
    	   LiveWindow.run();
    	   //LiveWindow.addSensor("Camera", 1, tServer);

           //arm at rest  
    	   System.out.println("arm code");
    	   if(!(armUpDrive.get()||armUpOp.get()||armDownDrive.get()||armDownOp.get()))
        	{
        		liftMain.set(0);
        		//liftSlave.set(0);
        	}
       		if(armUpDrive.get()||armUpOp.get())   //lift up  *7*
       		{
       			liftMain.set(P51RobotDefine.armUpSpeed);
       			//liftSlave.set(P51RobotDefine.armUpSpeed);
       			System.out.println("Arm up");
       		}
       		if(armDownDrive.get()||armDownOp.get()) //lift down *8*
       		{
       			liftMain.set(P51RobotDefine.armDownSpeed);
       			//liftSlave.set(P51RobotDefine.armDownSpeed);
        		System.out.println("Arm down");
        	}
       		
       		System.out.println("Mecanum code");
        	//Mecanum drive Switch with buttons 11 & 12
        	if (cartesianDriver.get()||cartesianOp.get())//Cartesian on *12*
        	{
        		polarDrive=false;
        		System.out.println("Polar Drive Off");
        		cartesianDrive=true;
        		System.out.println("Cartesian Drive On");
        		SmartDashboard.putBoolean("Polar drive", polarDrive);
        		SmartDashboard.putBoolean("Cartesian drive", cartesianDrive);
        	}
        	if (polarDriver.get()||polarOp.get())//Polar on *11*
        	{
        		polarDrive=true;
        		System.out.println("Polar Drive On");
        		cartesianDrive=false;
        		System.out.println("Cartesian Drive Off");
        		SmartDashboard.putBoolean("Polar drive", polarDrive);
        		SmartDashboard.putBoolean("Cartesian drive", cartesianDrive);
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
        			System.out.println("driving the robot");
        			myRobot.mecanumDrive_Polar(RobotDriveMath.polarMagnitude(stick.getX(),stick.getY()),
        					RobotDriveMath.polarAngle(stick.getX(),stick.getY()),
        					RobotDriveMath.twistWithDeadband(stick.getTwist()));
        		}
        		if(cartesianDrive)
        			System.out.println("cartesian driving the robot");
        			myRobot.mecanumDrive_Cartesian(RobotDriveMath.xWithDeadband(stick.getX()),
        					RobotDriveMath.yWithDeadband(stick.getY()),
        					RobotDriveMath.twistWithDeadband(stick.getTwist()), gyro.getAngle());
        	}
        	
        	// PID Stuff
        	frontLeftSpeed = P51Drive.getSpeeds(P51RobotDefine.leftFrontMotorInt, stick.getX(), stick.getY(), stick.getTwist());
        	frontRightSpeed = P51Drive.getSpeeds(P51RobotDefine.rightFrontMotorInt, stick.getX(), stick.getY(), stick.getTwist());
        	backLeftSpeed = P51Drive.getSpeeds(P51RobotDefine.leftBackMotorInt, stick.getX(), stick.getY(), stick.getTwist());
        	backRightSpeed = P51Drive.getSpeeds(P51RobotDefine.rightBackMotorInt, stick.getX(), stick.getY(), stick.getTwist());
        	
        	System.out.println("PID Stuff");
        	if(SmartDashboard.getBoolean("PID") && runPID)
        	{
        		if (PIDNeedsInit)
        		{
        			frontLeft.changeControlMode(ControlMode.valueOf(2));
        	    	frontRight.changeControlMode(ControlMode.valueOf(2));
        	    	backLeft.changeControlMode(ControlMode.valueOf(2));
        	    	backRight.changeControlMode(ControlMode.valueOf(2));
        	    	liftMain.changeControlMode(ControlMode.valueOf(1));
        	    	PIDNeedsInit = false;
        		}
        		frontLeft.setPID(SmartDashboard.getNumber("P Value"), SmartDashboard.getNumber("I Value"), SmartDashboard.getNumber("D Value"));
        		frontRight.setPID(SmartDashboard.getNumber("P Value"), SmartDashboard.getNumber("I Value"), SmartDashboard.getNumber("D Value"));
        		backLeft.setPID(SmartDashboard.getNumber("P Value"), SmartDashboard.getNumber("I Value"), SmartDashboard.getNumber("D Value"));
        		backRight.setPID(SmartDashboard.getNumber("P Value"), SmartDashboard.getNumber("I Value"), SmartDashboard.getNumber("D Value"));
        		liftMain.setPID(SmartDashboard.getNumber("P Value"), SmartDashboard.getNumber("I Value"), SmartDashboard.getNumber("D Value"));
        		
        		System.out.println("P Value: "+ SmartDashboard.getNumber("P Value"));
        		System.out.println("I Value: "+ SmartDashboard.getNumber("I Value"));
        		System.out.println("D Value: "+ SmartDashboard.getNumber("D Value"));
        	
        		SmartDashboard.putString(P, Double.toString(SmartDashboard.getNumber("P Value")));
        		SmartDashboard.putString(I, Double.toString(SmartDashboard.getNumber("I Value")));
        		SmartDashboard.putString(D, Double.toString(SmartDashboard.getNumber("D Value")));
        	}
        	if (PIDDeacivate)// turn off all PIDS
        	{
        		frontLeft.changeControlMode(ControlMode.valueOf(15));	//15 == Disable
            	frontRight.changeControlMode(ControlMode.valueOf(15));
            	backLeft.changeControlMode(ControlMode.valueOf(15));
            	backRight.changeControlMode(ControlMode.valueOf(15));
            	liftMain.changeControlMode(ControlMode.valueOf(15));
            	SmartDashboard.putBoolean("PID", false);
            	PIDDeacivate = false;
        	}
        	if(PIDControlOn.get())	//*5*
        		runPID = true;
        	if(PIDControlOff.get())	//*6*
        	{
        		runPID = false;
        		PIDNeedsInit = true;
        		PIDDeacivate = true;
        	}
        	
        	
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
            
            SmartDashboard.putBoolean("Compressor On", myCompressor.getPressureSwitchValue());
            
            //Actuate Solenoid for claw
        	if(clawDrive.get()||clawOp.get()||clawTrigger.get())	//*10* or *1*
        	{
        		
        		mySolenoid.set(Value.kForward);
            	System.out.println("claw closed");
        	}
            else
            {
            	mySolenoid.set(Value.kReverse);
            	System.out.println("claw open");
            }
        	
        	if (deadbandUp.update(deadbandInc.get()))	//*3*
        	{
        		P51RobotDefine.deadbandExponent += 0.25;
        		System.out.println("Inc to Deadband exp = " + P51RobotDefine.deadbandExponent);
        		SmartDashboard.putNumber("Deadband Exponent", P51RobotDefine.deadbandExponent);
        	}
        	if (deadbandDown.update(deadbandDec.get()))	//*4*
        	{
        		if (P51RobotDefine.deadbandExponent>1.25)
        			{
        				P51RobotDefine.deadbandExponent -= 0.25;
        				System.out.println("Dec to Deadband exp = " + P51RobotDefine.deadbandExponent);
        				SmartDashboard.putNumber("Deadband Exponent", P51RobotDefine.deadbandExponent);
        			}
        	}
        	
        	Timer.delay(0.005);		// wait for a motor update time
        }//end main while loop
    }
    
    
  //Comparator function for sorting particles. Returns true if particle 1 is larger
  		static boolean CompareParticleSizes(ParticleReport particle1, ParticleReport particle2)
  		{
  			//we want descending sort order
  			return particle1.PercentAreaToImageArea > particle2.PercentAreaToImageArea;
  		}

  		/**
  		 * Converts a ratio with ideal value of 1 to a score. The resulting function is piecewise
  		 * linear going from (0,0) to (1,100) to (2,0) and is 0 for all inputs outside the range 0-2
  		 */
  		double ratioToScore(double ratio)
  		{
  			return (Math.max(0, Math.min(100*(1-Math.abs(1-ratio)), 100)));
  		}

  		/**
  		 * Method to score convex hull area. This scores how "complete" the particle is. Particles with large holes will score worse than a filled in shape
  		 */
  		double ConvexHullAreaScore(ParticleReport report)
  		{
  			return ratioToScore((report.Area/report.ConvexHullArea)*1.18);
  		}

  		/**
  		 * Method to score if the particle appears to be a trapezoid. Compares the convex hull (filled in) area to the area of the bounding box.
  		 * The expectation is that the convex hull area is about 95.4% of the bounding box area for an ideal tote.
  		 */
  		double TrapezoidScore(ParticleReport report)
  		{
  			return ratioToScore(report.ConvexHullArea/((report.BoundingRectRight-report.BoundingRectLeft)*(report.BoundingRectBottom-report.BoundingRectTop)*.954));
  		}

  		/**
  		 * Method to score if the aspect ratio of the particle appears to match the long side of a tote.
  		 */
  		double LongSideScore(ParticleReport report)
  		{
  			return ratioToScore(((report.BoundingRectRight-report.BoundingRectLeft)/(report.BoundingRectBottom-report.BoundingRectTop))/LONG_RATIO);
  		}

  		/**
  		 * Method to score if the aspect ratio of the particle appears to match the short side of a tote.
  		 */
  		double ShortSideScore(ParticleReport report){
  			return ratioToScore(((report.BoundingRectRight-report.BoundingRectLeft)/(report.BoundingRectBottom-report.BoundingRectTop))/SHORT_RATIO);
  		}

  		/**
  		 * Computes the estimated distance to a target using the width of the particle in the image. For more information and graphics
  		 * showing the math behind this approach see the Vision Processing section of the ScreenStepsLive documentation.
  		 *
  		 * @param image The image to use for measuring the particle estimated rectangle
  		 * @param report The Particle Analysis Report for the particle
  		 * @param isLong Boolean indicating if the target is believed to be the long side of a tote
  		 * @return The estimated distance to the target in feet.
  		 */
  		double computeDistance (Image image, ParticleReport report, boolean isLong) {
  			double normalizedWidth, targetWidth;
  			NIVision.GetImageSizeResult size;

  			size = NIVision.imaqGetImageSize(image);
  			normalizedWidth = 2*(report.BoundingRectRight - report.BoundingRectLeft)/size.width;
  			targetWidth = isLong ? 26.0 : 16.9;

  			return  targetWidth/(normalizedWidth*12*Math.tan(VIEW_ANGLE*Math.PI/(180*2)));
  		}
    
    
    
	/**
     * Runs during test mode
     */
    public void test() 
    {
    	
    }
}
