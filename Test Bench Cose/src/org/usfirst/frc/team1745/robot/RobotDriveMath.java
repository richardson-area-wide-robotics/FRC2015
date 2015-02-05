/**
 * 
 */
package org.usfirst.frc.team1745.robot;

import org.usfirst.frc.team1745.robot.P51RobotDefine;

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
	
	public RobotDriveMath()
	{
	}
	
	public static double xWithDeadband(double xInput)
	{
		if(xInput>.05)
			xAxis=Math.pow((xInput-.05)/(.95),P51RobotDefine.deadbandExponent);
		if(xInput<-.05)
			xAxis=Math.pow((xInput+.05)/(.95),P51RobotDefine.deadbandExponent);
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
		//angle = (Math.asin(yWithDeadband(yInput)/magnitude)*180/3.14159) + 90;
		return angle;
		
	}
}