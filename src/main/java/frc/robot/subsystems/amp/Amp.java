package frc.robot.subsystems.amp;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Amp extends SubsystemBase {
  private Solenoid wristSolenoid;
  private Solenoid elbowSolenoid;
  // private Compressor compressor;

  public Amp() {
    // compressor = new Compressor(20, PneumaticsModuleType.REVPH);
    elbowSolenoid = new Solenoid(20, PneumaticsModuleType.REVPH, 2);
    wristSolenoid = new Solenoid(20, PneumaticsModuleType.REVPH, 3);
    // compressor.enableAnalog(110, 120);
  }

  @Override
  public void periodic() {
    // System.out.println(compressor.getConfigType());
    // SmartDashboard.putNumber("Compresser Pressure", compressor.getPressure());
    // This method will be called once per scheduler run
  }

  public void closeWrist() {
    wristSolenoid.set(true);
  }

  public void openWrist() {
    wristSolenoid.set(false);
  }

  public void activateElbow() {
    elbowSolenoid.set(true);
  }

  public void deactivateElbow() {
    elbowSolenoid.set(false);
  }

  public void reset() {
    openWrist();
    deactivateElbow();
  }
}
