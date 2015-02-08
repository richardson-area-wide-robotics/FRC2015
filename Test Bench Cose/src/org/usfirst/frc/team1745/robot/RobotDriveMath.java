/**
 * 
 */
package org.usfirst.frc.team1745.robot;

import org.usfirst.frc.team1745.robot.P51RobotDefine;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;

/**
 * @author Adam
 * Contains the math necessary for dead-bands and polar drive
 */
public class RobotDriveMath extends Robot
{
	
	public static double xAxis;
    public static double yAxis;
    public static double twist;
    public static double magnitude;
    public static double angle;
    
    PIDController frontLeftPID;
    PIDController frontRightPID;
    PIDController backLeftPID;
    PIDController backRightPID;
    
    static final int kFrontLeft_val = 0;
    static final int kFrontRight_val = 1;
    static final int kRearLeft_val = 2;
    static final int kRearRight_val = 3;
	
	public RobotDriveMath()
	{
	}
	
	public static double xWithDeadband(double xInput)
	{
		if(xInput>.05)
			xAxis=Math.pow((xInput-.05)/(.95),P51RobotDefine.deadbandExponent);
		if(xInput<-.05)
		{
			xAxis=Math.pow((xInput+.05)/(.95),P51RobotDefine.deadbandExponent);
			if (xAxis>0)
			xAxis= -xAxis;
		}
		 return xAxis;
	}
	
	public static double yWithDeadband(double yInput)
	{
		if(yInput>.05)
			yAxis=Math.pow((yInput-.05)/(.95),P51RobotDefine.deadbandExponent);
		if(yInput<-.05)
			yAxis=Math.pow((yInput+.05)/(.95),P51RobotDefine.deadbandExponent);
		return yAxis;
	}
	
	public static double twistWithDeadband(double twistInput)
	{
		if(twistInput>.05)
			twist=Math.pow((twistInput-.05)/(.95),P51RobotDefine.deadbandExponent);
		if(twistInput<-.05)
			twist=Math.pow((twistInput+.05)/(.95),P51RobotDefine.deadbandExponent);
		return twist;
	}
	
	public static double polarMagnitude(double xInput,double yInput)
	{
		magnitude = Math.sqrt(Math.pow(xWithDeadband(xInput),2)+Math.pow(yWithDeadband(yInput),2));
		return magnitude;
	}
	
	public static double polarAngle(double xInput,double yInput)
	{
		angle = ((Math.atan2(yWithDeadband(yInput),xWithDeadband(xInput)))*180/(Math.PI)) + 90;
		return angle;
		
	}
}