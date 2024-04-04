package frc.robot.subsystems.amp;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Amp extends SubsystemBase {
  private DoubleSolenoid wristSolenoid;
  private DoubleSolenoid elbowSolenoid;

  public Amp() {
    wristSolenoid = new DoubleSolenoid(20, PneumaticsModuleType.REVPH, 2, 3);
    elbowSolenoid = new DoubleSolenoid(20, PneumaticsModuleType.REVPH, 0, 1);
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
