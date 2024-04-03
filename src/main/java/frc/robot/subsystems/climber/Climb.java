package frc.robot.subsystems.climber;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climb extends SubsystemBase {
  private DoubleSolenoid climbSolenoid;
  private Compressor compressor;
  private PneumaticHub hub;

  public Climb() {
    hub = new PneumaticHub(20);

    compressor = new Compressor(20, PneumaticsModuleType.REVPH);
    // climbSolenoid = new DoubleSolenoid(20, PneumaticsModuleType.REVPH, 4, 5);
    // climbSolenoid = new DoubleSolenoid(20, PneumaticsModuleType.REVPH, 0, 1);
    compressor.enableAnalog(110, 120);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Compresser Pressure", compressor.getPressure());
    // This method will be called once per scheduler run
  }

  // Put methods for controlling this subsystem here. Call these from Commands.
  public void climberDown() {
    climbSolenoid.set(DoubleSolenoid.Value.kForward);
  }

  public void climberUp() {
    climbSolenoid.set(DoubleSolenoid.Value.kReverse);
  }

  // public void stop() {
  //   climbSolenoid.set(DoubleSolenoid.Value.kOff);
  // }
}
