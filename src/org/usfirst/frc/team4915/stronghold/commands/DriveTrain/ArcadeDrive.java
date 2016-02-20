package org.usfirst.frc.team4915.stronghold.commands.DriveTrain;
import java.util.List;

import org.usfirst.frc.team4915.stronghold.ModuleManager;
import org.usfirst.frc.team4915.stronghold.Robot;
import org.usfirst.frc.team4915.stronghold.RobotMap;
import org.usfirst.frc.team4915.stronghold.utils.BNO055;
import org.usfirst.frc.team4915.stronghold.vision.robot.VisionState;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ArcadeDrive extends Command {

    public Joystick joystickDrive;
    private double joystickX;
    private double joystickY;
    public static List<CANTalon> motors = Robot.driveTrain.motors;
    public double[] oldVelocity = new double[3];
    public double[] distTraveled = new double[3];
    public double distFromOrigin;

    public ArcadeDrive() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.driveTrain);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    	 for (int i = 0; i < motors.size(); i++) {
             motors.get(i).setEncPosition(0);
    	 }
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        this.joystickDrive = Robot.oi.getJoystickDrive();
        this.joystickX = this.joystickDrive.getAxis(Joystick.AxisType.kX);
        this.joystickY = this.joystickDrive.getAxis(Joystick.AxisType.kY);
        
        if (ModuleManager.GYRO_MODULE_ON){
            Robot.driveTrain.trackGyro();
        }


        Robot.driveTrain.joystickThrottle = Robot.driveTrain.modifyThrottle();

        
        if (!VisionState.getInstance().followTargetX(Robot.driveTrain) ){
    	   if ((Math.abs(this.joystickX) < Math.abs(0.075)) && (Math.abs(this.joystickY) < Math.abs(0.075))) {
               Robot.driveTrain.stop();
           } 
    	   else {
               Robot.driveTrain.arcadeDrive(this.joystickDrive);
    	   }
    	   SmartDashboard.putNumber("Drive joystick X position", this.joystickX);
    	   SmartDashboard.putNumber("Drive joystick Y position", this.joystickY);
    	   
    	   BNO055.CalData calData = RobotMap.imuEuler.getCalibration();
    	   int num = (int)(.5 + RobotMap.imuEuler.getHeading());
    	   distFromOrigin = BNO055.getInstance().getDistFromOrigin();
    	   
    	   SmartDashboard.putNumber("DistFromOrigin", distFromOrigin);
    	   SmartDashboard.putBoolean("IMU present", RobotMap.imuEuler.isSensorPresent());
    	   SmartDashboard.putBoolean("IMU initialized", RobotMap.imuEuler.isInitialized());
    	   SmartDashboard.putNumber("IMU heading", num);
    	   SmartDashboard.putNumber("IMU calibration status", (1000 + (calData.accel * 100) + calData.gyro *10 + calData.mag)); //Calibration values range from 0-3, Right to left: mag, gyro, accel
       }
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    	Robot.driveTrain.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    	end();
    }
    
  //Call every 100th of a sec
    

}
