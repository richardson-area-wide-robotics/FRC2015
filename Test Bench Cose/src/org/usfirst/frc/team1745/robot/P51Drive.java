package org.usfirst.frc.team1745.robot;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.MotorSafety;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tInstances;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tResourceType;

public class P51Drive extends RobotDrive implements MotorSafety {

	public P51Drive(SpeedController frontLeftMotor,
			SpeedController rearLeftMotor, SpeedController frontRightMotor,
			SpeedController rearRightMotor) {
		super(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);	

	}
    static final int kFrontLeft_val = 0;
    static final int kFrontRight_val = 1;
    static final int kRearLeft_val = 2;
    static final int kRearRight_val = 3;

	public static double getSpeeds(int motor, double x, double y, double twist)
	{
		double[] wheelSpeeds = new double[4];
		if (Robot.polarDrive)
		{
			double direction = RobotDriveMath.polarAngle(x, y);
			//double frontLeftSpeed, rearLeftSpeed, frontRightSpeed, rearRightSpeed;
	        // Normalized for full power along the Cartesian axes.
			
			double rotation = RobotDriveMath.twistWithDeadband(twist);
			double magnitude = RobotDriveMath.polarMagnitude(x, y);
	        magnitude = limit(magnitude) * Math.sqrt(2.0);
	        // The rollers are at 45 degree angles.
	        double dirInRad = (direction + 45.0) * 3.14159 / 180.0;
	        double cosD = Math.cos(dirInRad);
	        double sinD = Math.sin(dirInRad);

			wheelSpeeds[kFrontLeft_val] = (sinD * magnitude + rotation);
	        wheelSpeeds[kFrontRight_val] = (cosD * magnitude - rotation);
	        wheelSpeeds[kRearLeft_val] = (cosD * magnitude + rotation);
	        wheelSpeeds[kRearRight_val] = (sinD * magnitude - rotation);
		}
		
		if(Robot.cartesianDrive)
		{
	        double xIn = x;
	        double yIn = y;
	        // Negate y for the joystick.
	        yIn = -yIn;
	        // Compenstate for gyro angle.
	        double rotated[] = rotateVector(xIn, yIn, Robot.gyro.getAngle());
	        xIn = rotated[0];
	        yIn = rotated[1];
	        double rotation = RobotDriveMath.twistWithDeadband(twist);

	        wheelSpeeds[kFrontLeft_val] = xIn + yIn + rotation;
	        wheelSpeeds[kFrontRight_val] = -xIn + yIn - rotation;
	        wheelSpeeds[kRearLeft_val] = -xIn + yIn + rotation;
	        wheelSpeeds[kRearRight_val] = xIn + yIn - rotation;
		}
		
		return wheelSpeeds[motor];
	}

	@Override
	public void setExpiration(double timeout) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getExpiration() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isAlive() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void stopMotor() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSafetyEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSafetyEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}
}

