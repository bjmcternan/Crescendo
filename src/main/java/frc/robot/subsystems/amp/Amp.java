package frc.robot.subsystems.amp;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Amp extends SubsystemBase {
  private DoubleSolenoid wristSolenoid;
  private DoubleSolenoid elbowSolenoid;
  // private Compressor compressor;

  public Amp() {
    // compressor = new Compressor(20, PneumaticsModuleType.REVPH);
    wristSolenoid = new DoubleSolenoid(20, PneumaticsModuleType.REVPH, 2, 3);
    elbowSolenoid = new DoubleSolenoid(20, PneumaticsModuleType.REVPH, 0, 1);
    // compressor.enableAnalog(110, 120);
  }

  @Override
  public void periodic() {
    // System.out.println(compressor.getConfigType());
    // SmartDashboard.putNumber("Compresser Pressure", compressor.getPressure());
    // This method will be called once per scheduler run
  }

  public void closeWrist() {
    wristSolenoid.set(DoubleSolenoid.Value.kReverse);
  }

  public void openWrist() {
    wristSolenoid.set(DoubleSolenoid.Value.kForward);
  }

  public void activateElbow() {
    elbowSolenoid.set(DoubleSolenoid.Value.kReverse);
  }

  public void deactivateElbow() {
    elbowSolenoid.set(DoubleSolenoid.Value.kForward);
  }

  public void reset() {
    openWrist();
    deactivateElbow();
  }
}
