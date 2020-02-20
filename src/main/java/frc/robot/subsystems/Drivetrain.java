// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package frc.robot.subsystems;


import frc.robot.commands.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;

import edu.wpi.first.wpilibj.Joystick;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SerialPort;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS

/**
 *
 */
public class Drivetrain extends Subsystem {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
private WPI_TalonFX leftTalonLead;
private WPI_TalonFX leftTalonFollower;
private WPI_TalonFX rightTalonLead;
private WPI_TalonFX rightTalonFollower;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS


    private double kF = 0.04759;
    private double kP = 0.01461;
    private double kI = 0.;
    private double kD = 0.;

    private double Ldeadband = .15;
    private double Rdeadband = .15;

    private double speed = 1;
    private double maxVel = 21549;

    private final double INVALID_INPUT = -99;

    

    public Drivetrain() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
leftTalonLead = new WPI_TalonFX(0);


        
leftTalonFollower = new WPI_TalonFX(1);


        
rightTalonLead = new WPI_TalonFX(2);


        
rightTalonFollower = new WPI_TalonFX(3);


        

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
    
    
    leftTalonLead.clearStickyFaults();
    leftTalonFollower.clearStickyFaults();
    rightTalonLead.clearStickyFaults();
    rightTalonFollower.clearStickyFaults();

        //Set facotry defaults for onboard PID
    leftTalonLead.configFactoryDefault();
    rightTalonLead.configFactoryDefault();
    

    leftTalonFollower.follow(leftTalonLead);
    rightTalonFollower.follow(rightTalonLead);

    leftTalonLead.setInverted(true);
    leftTalonFollower.setInverted(InvertType.FollowMaster);
    rightTalonLead.setInverted(false);
    rightTalonFollower.setInverted(InvertType.FollowMaster);

    leftTalonLead.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 4000);
    rightTalonLead.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 4000);

    //Configure PID
    leftTalonLead.config_kF(0,kF,10);
    leftTalonLead.config_kP(0,kP,10);
    leftTalonLead.config_kI(0,kI,10);
    leftTalonLead.config_kD(0,kD,0);
    leftTalonLead.configClosedloopRamp(0.5);
    leftTalonLead.configMotionCruiseVelocity((int)(maxVel * 0.5));
    leftTalonLead.configMotionAcceleration((int)(maxVel * 0.5));

    rightTalonLead.config_kF(0,kF,10);
    rightTalonLead.config_kP(0,kP,10);
    rightTalonLead.config_kI(0,kI,10);
    rightTalonLead.config_kD(0,kD,0);
    rightTalonLead.configClosedloopRamp(0.5);
    rightTalonLead.configMotionCruiseVelocity((int)(maxVel * 0.5));
    rightTalonLead.configMotionAcceleration((int)(maxVel * 0.5));

    rightTalonLead.setNeutralMode(NeutralMode.Coast);
    leftTalonLead.setNeutralMode(NeutralMode.Coast);

    rightTalonLead.configNeutralDeadband(.01);
    rightTalonFollower.configNeutralDeadband(.01);
    leftTalonLead.configNeutralDeadband(.01);
    leftTalonFollower.configNeutralDeadband(.01);
    
    }

    @Override
    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new VelocityDrive());
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop
        SmartDashboard.putNumber("Left Encoder Velocity", leftTalonLead.getSensorCollection().getIntegratedSensorVelocity());
        SmartDashboard.putNumber("Left Encoder Position", leftTalonLead.getSensorCollection().getIntegratedSensorPosition());
        SmartDashboard.putNumber("Right Encoder Velocity", leftTalonLead.getSensorCollection().getIntegratedSensorVelocity());
        SmartDashboard.putNumber("Right Encoder Position", leftTalonLead.getSensorCollection().getIntegratedSensorPosition());

        //SmartDashboard.putNumber("Gyro Angle",gyro.getAngle());
        //SmartDashboard.putNumber("Gyro Yaw", gyro.getYaw());
        //SmartDashboard.putNumber("Gyro Pitch", gyro.getPitch());
    }

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CMDPIDGETTERS


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CMDPIDGETTERS


    // Put methods for controlling this subsystem
    // here. Call these from Commands.


    // Converts joystick input adjusted for deadband to current for the motor
    public void dumbDrive(double left, double right) {

        double leftPos = left;
        double rightPos = right;
        double retval = 0.0;
        // Running at half speed as to not kill people
        retval = calcMotorPower(leftPos, Ldeadband);
        if(INVALID_INPUT == retval) {
            System.out.println("Invalid left motor input" + leftPos);
        } else {
            leftTalonLead.set(TalonFXControlMode.PercentOutput,retval*.5);    
        }

        retval = calcMotorPower(rightPos, Rdeadband);
        if(INVALID_INPUT == retval) {
            System.out.println("Invalid right motor input" + rightPos);
        } else {
            rightTalonLead.set(TalonFXControlMode.PercentOutput,retval * speed);    
        }   
    }

    // Converts joystick input adjusted to a RPM for the Falcon's PIDF loop to aim for
    public void velocityDrive(double leftPos, double rightPos){

        double retval = 0.0;

        retval = calcMotorPower(leftPos, Ldeadband);
        if(INVALID_INPUT == retval) {
            System.out.println("Invalid left motor input" + leftPos);
        } else {
            leftTalonLead.set(TalonFXControlMode.Velocity,(retval * maxVel * speed));    
        }

        retval = calcMotorPower(rightPos, Rdeadband);
        if(INVALID_INPUT == retval) {
            System.out.println("Invalid right motor input" + rightPos);
        } else {
            rightTalonLead.set(TalonFXControlMode.Velocity,(retval * maxVel * speed));
        }
    }

    // Stops motor usually used after the drive command ends to prevent shenanigans
    public void stop() {
        leftTalonLead.set(TalonFXControlMode.Current,0);
        rightTalonLead.set(TalonFXControlMode.Current,0);
    }

    //Calculates the motor power to use based on a given deadband and joystick input from -1 to 1
    //Prevents spikes in motor power by calculating the line to use where 0 is the deadband and 1 is the max
    public double calcMotorPower(double input, double deadband) {
        double retval = 0.0;
        if(Math.abs(input) <= deadband) { //Check if input is inside the deadband
            return 0;
        }

        if((input < -1) || (input > 1)) { //input must be between -1 and 1
            return INVALID_INPUT;
        }
        
        retval = (1/(1 - deadband) * Math.abs(input) - (deadband/(1 - deadband)));

        if(input < 0) {
           return -1 * retval;
        } else {
            return retval;
        }
    }

    //Velocity Drive without Deadband for vision purposes
    public void visionDrive(double left, double right){
        if (left > 1 || left < -1){
            System.out.println("Invalid left motor input " + left);
            left = 0;
        }
        if (right > 1 || right < -1){
            System.out.println("Invalid right motor input " + left);
            right = 0;
        }
        leftTalonLead.set(TalonFXControlMode.Velocity,(left * maxVel * speed));
        rightTalonLead.set(TalonFXControlMode.Velocity,(right * maxVel * speed));
    }
}

