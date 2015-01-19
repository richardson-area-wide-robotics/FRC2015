/**
 * 
 */
package org.usfirst.frc.team1745.robot;

/**
 * @author staurt
 *Detailed defined physical description of the Robot
 *
 *
 */
public final class P51RobotDefine
{
	enum CircuitBrakerSize {TWENTYAMP, THIRTYAMP, FOURTYAMP};
	enum MotorTypes {BAGMOTOR, MINICIMMOTOR, CIMMOTOR};
	enum GearBoxType {VERSAPLANETARY1to12, VERSAPLANETARY1to63};
	
	//RoboRio CANIDs
	public static final int UNUSED_CANID = 0;
	public static final int PDP_CANID = 1;
	public static final int PCM_CANID = 2;
	//Motor CAN IDs
	public static final int leftFrontMecanum_CANID = 3;
	public static final int rightFrontMecanum_CANID = 4;
	public static final int leftBackMecanum_CANID = 5;
	public static final int rightBackMecanum_CANID = 6;
	public static final int wench_CANID = 7;
	
	//Motor PDP Channel Locations
	public static final int leftFrontMecanum_PDPLoc = 1;
	public static final int rightFrontMecanum_PDPLoc = 3;
	public static final int leftBackMecanum_PDPLoc = 5;
	public static final int rightBackMecanum_PDPLoc = 7;
	public static final int wench_PDPLoc = 8;
	
	//Circuit Breaker values
	public static final CircuitBrakerSize leftFrontMecanum_CircutBreaker = CircuitBrakerSize.FOURTYAMP;
	public static final CircuitBrakerSize rightFrontMecanum_CircutBreaker = CircuitBrakerSize.FOURTYAMP;
	public static final CircuitBrakerSize leftBackMecanum_CircutBreaker = CircuitBrakerSize.FOURTYAMP;
	public static final CircuitBrakerSize rightBackMecanum_CircutBreaker = CircuitBrakerSize.FOURTYAMP;
	public static final CircuitBrakerSize wench_CircutBreaker = CircuitBrakerSize.FOURTYAMP;
	
	//Motor Types
	
	//Gearbox Types
	
	//Solenoid Channel Locations
	public static final int clawSolenoidForward_PCMChan = 0;
	public static final int clawSolenoidBackwards_PCMChan = 1;
	
	//Sensor Channel Locations
	public static final int gyro_ANAChan = 0;
	
	
	//OI Locations
	public static final int driver_USBJoyStick = 1;
	public static final int operator_USBJoyStick = 2;
	
	
	public P51RobotDefine()
	{
		
		// TODO Auto-generated constructor stub
	}


}
