// RobotBuilder Version: 3.1
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS

/**
 *
 */
public class Launcher extends SubsystemBase 
{
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANT
    private final int TARGET_DEADBAND = 150;
    private final int DEFAULT_TARGET_RPM = 2400;
    private int targetRPM = DEFAULT_TARGET_RPM;
    private final double GEAR_RATIO = 6.0/5.0;
    private final double CLOSED_LOOP_RAMPRATE = 0.5;
    private double lF = 0.05;
    private double lP = 0.1;
    private double lI = 0.0;
    private double lD = 0.0;
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private WPI_TalonFX launcherMotor;
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private DoubleSolenoid shooterPiston;
    private RobotContainer m_robotContainer;
    /**
    *
    */
    public Launcher() 
    {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        launcherMotor = new WPI_TalonFX(6); 
        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        DoubleSolenoid shooterPiston = new DoubleSolenoid(0, 3, 4);
        addChild("shooterPiston", shooterPiston);

        launcherMotor.clearStickyFaults();
        launcherMotor.configFactoryDefault();
        launcherMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
        launcherMotor.config_kF(0, lF, 10);
        launcherMotor.config_kP(0, lP, 10);
        launcherMotor.config_kI(0, lI, 10);
        launcherMotor.config_kD(0, lD, 10);
        launcherMotor.setInverted(true);
        launcherMotor.configClosedloopRamp(CLOSED_LOOP_RAMPRATE);
        
        SmartDashboard.putNumber("Target RPM", targetRPM);
    }

    @Override
    public void periodic() 
    {
        int newTargetRPM = (int)SmartDashboard.getNumber("Target RPM", -1);
        int targetRPMbtn1 = (int)SmartDashboard.getNumber("Button 1 target", -1);
        int targetRPMbtn2 = (int)SmartDashboard.getNumber("Button 2 target", -1);
        int targetRPMbtn3 = (int)SmartDashboard.getNumber("Button 3 target", -1);
        int targetRPMbtn4 = (int)SmartDashboard.getNumber("Button 4 target", -1);
    
        // Put code here to be run every loop
        // Update motor speed here IF SHOOTER IS ON
        
        if (newTargetRPM < 0)
        {
            System.out.println("Launcher - invalid targetRPM: " + newTargetRPM);
        } else
        {
            if (newTargetRPM != targetRPM)
            {
                targetRPM = newTargetRPM;
            }
        }

    }
    
    @Override
    public void simulationPeriodic() 
    {
        // This method will be called once per scheduler run when in simulation
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private double convertRPMtoVelocity(double RPM) 
    {
        double conversionVel = RPM / 60 / 1000 * 2048 * GEAR_RATIO * 100;
        return conversionVel;
    }

    public void enableLauncher(double rpm) 
    {
        double velSpeed = convertRPMtoVelocity(rpm);

        SmartDashboard.putNumber("Target Vel", velSpeed);
        launcherMotor.set(ControlMode.Velocity, velSpeed);
    }

    public void enableLauncher() 
    {
        enableLauncher(targetRPM);
    }

    public void disableLauncher() 
    {
        launcherMotor.set(0);
    }

    private double convertVelocityToRPM(double velocity) 
    {
        double conversionRPM = velocity * 1000 * 60 / 2048 * (1 / GEAR_RATIO) / 100;
        return conversionRPM;
    }

    // Not being used currently
    public boolean isReady() 
    {
        double currentRPM = convertVelocityToRPM(launcherMotor.getSelectedSensorVelocity(0));
        if ((currentRPM + TARGET_DEADBAND) >= (targetRPM)
                && (currentRPM - TARGET_DEADBAND) <= (targetRPM))
        {
            SmartDashboard.putNumber("Current RPM", currentRPM);
            SmartDashboard.putBoolean("IsLauncherReady?", true);
            return true;
        } 
        else 
        {
            SmartDashboard.putNumber("Current RPM", currentRPM);
            SmartDashboard.putBoolean("IsLauncherReady?", false);
            return false;
        }
    }

    public void dumbLauncher(){
        launcherMotor.set(0.9);
    }

    public void shooterPistonUp()
    {
        shooterPiston.set(Value.kForward);
    }

    public void shooterPistonDown()
    {
        shooterPiston.set(Value.kReverse);
    }
}
