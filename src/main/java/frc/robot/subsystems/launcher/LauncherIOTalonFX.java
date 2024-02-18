package frc.robot.subsystems.launcher;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.math.util.Units;

public class LauncherIOTalonFX implements LauncherIO {
  public final TalonFX motor;

  private final StatusSignal<Double> motorVelocity;
  private final StatusSignal<Double> motorAppliedVolts;
  private final StatusSignal<Double> motorCurrent;

  private double setPoint = 0.0d;
  private double setPointError = 0.0d;
  private final double gearRatio = (11.0d / 10.0d);

  public LauncherIOTalonFX(int canId) {
    this.motor = new TalonFX(canId);
    var config = new TalonFXConfiguration();

    config.Slot0.kP = 0.1d; // TODO: config
    config.Slot0.kI = 0.0d;
    config.Slot0.kD = 0.0d;
    config.Slot0.kV = 0.242d; // vols per RPS
    config.Slot0.kS = 0.32d;
    motor.getConfigurator().apply(config);

    motorVelocity = motor.getVelocity();
    motorAppliedVolts = motor.getMotorVoltage();
    motorCurrent = motor.getStatorCurrent();

    BaseStatusSignal.setUpdateFrequencyForAll(50, motorVelocity, motorAppliedVolts, motorCurrent);
    motor.optimizeBusUtilization();
  }

  @Override
  public void updateInputs(LauncherIOInputs inputs) {
    BaseStatusSignal.refreshAll(motorVelocity, motorAppliedVolts, motorCurrent);

    inputs.motorVelocityRadPerSec = Units.rotationsToRadians(motorVelocity.getValueAsDouble());
    inputs.motorAppliedVolts = motorAppliedVolts.getValueAsDouble();
    inputs.motorCurrentAmps = motorCurrent.getValueAsDouble();
    inputs.setPoint = setPoint;
    inputs.setPointError = motor.getClosedLoopError().getValueAsDouble();
  }

  @Override
  public void setSpeed(double speed) {
    setPoint = speed * gearRatio;
    motor.setControl(new VelocityVoltage(setPoint).withSlot(0));
  }
}
