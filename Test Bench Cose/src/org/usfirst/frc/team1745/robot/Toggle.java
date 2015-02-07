package org.usfirst.frc.team1745.robot;

public class Toggle {
	private boolean toggleValue; // the current value of the toggle 
	private boolean pastValue = false;
	public Toggle(boolean toggleValue) {
		super();
		this.toggleValue = toggleValue;
	}

	public boolean update(boolean newValue) {
		if( newValue == false && pastValue == true) // it was pressed now it is not
			toggleValue = true;
		else
			toggleValue = false;
			
		pastValue= newValue;
		return toggleValue;
	}
	

}
