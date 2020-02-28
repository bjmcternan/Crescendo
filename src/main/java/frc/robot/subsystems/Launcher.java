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

import frc.robot.Robot;
import frc.robot.commands.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS

/**
 *
 */
public class Launcher extends Subsystem {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANT
    private final int TARGET_DEADBAND = 150;
    private final int realWorldError = -45; 
    private final int DEFAULT_TARGET_RPM = 2400;
    private int targetRPM = DEFAULT_TARGET_RPM;
    private final double GEAR_RATIO = 42 / 30;
    private final String TARGET_RPM_STRING = "Target RPM";
    private double kF = 0.035;
    private double kP = 0.087;
    private double kI = 0.0;
    private double kD = 0.87;
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
private WPI_TalonFX launcherMotor;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public Launcher() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
launcherMotor = new WPI_TalonFX(10);


        

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        launcherMotor.configFactoryDefault();
        launcherMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        launcherMotor.config_kF(0, kF, 10);
        launcherMotor.config_kP(0, kP, 10);
        launcherMotor.config_kI(0, kI, 10);
        launcherMotor.config_kD(0, kD, 10);
        launcherMotor.setInverted(false);

        SmartDashboard.putNumber(TARGET_RPM_STRING, targetRPM);
    }

    @Override
    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    @Override
    public void periodic() {
        int newTargetRPM = (int)SmartDashboard.getNumber(TARGET_RPM_STRING, -1);
    
        // Put code here to be run every loop
        // Update motor speed here IF SHOOTER IS ON
        if (newTargetRPM < 0){
            System.out.println("Launcher - invalid targetRPM: " + newTargetRPM);
        } else{
            if (targetRPM != newTargetRPM){
                newTargetRPM = targetRPM;
            }
        }
        
    }

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CMDPIDGETTERS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CMDPIDGETTERS

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private double convertRPMtoVelocity(double RPM) {
        double conversionVel = RPM / 60 / 1000 * 4096 * GEAR_RATIO * 100;
        return conversionVel;
    }

    public void enableLauncher() {
        double velSpeed = convertRPMtoVelocity(targetRPM + realWorldError);

        SmartDashboard.putNumber("Target Vel", velSpeed);
        launcherMotor.set(ControlMode.Velocity, velSpeed);

    }

    public void disableLauncher() {
        launcherMotor.set(0);
    }

    private double convertVelocityToRPM(double velocity) {
        double conversionRPM = velocity * 1000 * 60 / 4096 * (1 / GEAR_RATIO) / 100;
        return conversionRPM;
    }

    public boolean isReady() {
        double currentRPM = convertVelocityToRPM(launcherMotor.getSelectedSensorVelocity(0));
        if ((currentRPM + TARGET_DEADBAND) >= (targetRPM)
                && (currentRPM - TARGET_DEADBAND) <= (targetRPM)){
            SmartDashboard.putNumber("Current RPM", currentRPM);
            SmartDashboard.putBoolean("IsLauncherReady?", true);
            return true;
        } else {
            SmartDashboard.putNumber("Current RPM", currentRPM);
            SmartDashboard.putBoolean("IsLauncherReady?", false);
            return false;
        }
    }
}
