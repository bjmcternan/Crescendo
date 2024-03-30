package frc.robot.subsystems.climber;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climb extends SubsystemBase {
  private DoubleSolenoid climbSolenoid;
  private DoubleSolenoid wrist;
  private Solenoid TREVOR;
  private Compressor compressor;

  public Climb() {
    // compressor = new Compressor(20, PneumaticsModuleType.REVPH);
    // climbSolenoid = new DoubleSolenoid(20, PneumaticsModuleType.REVPH, 0, 1);
    wrist = new DoubleSolenoid(20, PneumaticsModuleType.REVPH, 2, 3);
    // TREVOR = new Solenoid(20, PneumaticsModuleType.REVPH, 3);
    // compressor.enableAnalog(0, 2);
  }

  @Override
  public void periodic() {
    // System.out.println(compressor.getConfigType());
    // SmartDashboard.putNumber("Compresser Pressure", compressor.getPressure());
    // This method will be called once per scheduler run
  }

  // Put methods for controlling this subsystem here. Call these from Commands.
  public void climberDown() {
    // climbSolenoid.set(DoubleSolenoid.V
    wrist.set(DoubleSolenoid.Value.kReverse);
    // wrist.set(false);
    // TREVOR.set(false);
  }

  public void climberUp() {
    // climbSolenoid.set(DoubleSolenoid.Value.kForward);
    wrist.set(DoubleSolenoid.Value.kForward);
    // wrist.set(true);
    // TREVOR.set(true);
  }

  public void stop() {
    // climbSolenoid.set(DoubleSolenoid.Value.kOff);
    wrist.set(DoubleSolenoid.Value.kOff);
  }
}
