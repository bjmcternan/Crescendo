package frc.robot.subsystems.pneumatics;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SpikeCompressor extends SubsystemBase {
  private Compressor compressor;

  private static final double MIN_PRESSURE = 80;
  private static final double MAX_PRESSURE = 100;

  public SpikeCompressor() {
    compressor = new Compressor(20, PneumaticsModuleType.REVPH);
    enable();
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Compressor Pressure", getPressure());
  }

  public void enable() {
    compressor.enableAnalog(MIN_PRESSURE, MAX_PRESSURE);
  }

  public void disable() {
    compressor.disable();
  }

  public double getPressure() {
    return compressor.getPressure();
  }
}
