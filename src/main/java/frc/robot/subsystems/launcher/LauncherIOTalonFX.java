package frc.robot.subsystems.launcher;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class LauncherIOTalonFX implements LauncherIO {
  public final TalonFX motor;

  private final StatusSignal<Double> motorVelocity;
  // private final StatusSignal<Double> motorAppliedVolts;
  // private final StatusSignal<Double> motorCurrent;
  private final StatusSignal<Double> setPointError;
  private final StatusSignal<Double> rollerPosition;

  private double setPoint = 0.0d;
  private final double gearRatio = (11.0d / 10.0d);

  private static VoltageOut voltageOutCommand = new VoltageOut(0.0);
  private static VelocityVoltage velocityVoltageCommand = new VelocityVoltage(0.0).withSlot(0);

  public LauncherIOTalonFX(int canId) {
    this.motor = new TalonFX(canId);
    TalonFXConfiguration config = new TalonFXConfiguration();

    // Plastic gear config
    // config.Slot0.kP = 0.85;
    // config.Slot0.kI = 0.0;
    // config.Slot0.kD = 0.0;
    // config.Slot0.kV = 0.18;
    // config.Slot0.kS = 0.1d;

    // Metal gear config
    config.Slot0.kP = 0.3;
    config.Slot0.kI = 0.0;
    config.Slot0.kD = 0.0;
    config.Slot0.kV = 0.253;
    config.Slot0.kS = 0.69;

    config.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
    config.MotorOutput.NeutralMode = NeutralModeValue.Coast;
    config.CurrentLimits.StatorCurrentLimit = 100.0;
    config.CurrentLimits.StatorCurrentLimitEnable = true;
    motor.getConfigurator().apply(config);

    motor.setPosition(0.0);

    motorVelocity = motor.getVelocity();
    // motorAppliedVolts = motor.getMotorVoltage();
    // motorCurrent = motor.getStatorCurrent();
    setPointError = motor.getClosedLoopError();
    rollerPosition = motor.getPosition();

    BaseStatusSignal.setUpdateFrequencyForAll(50, motorVelocity, setPointError, rollerPosition);
    motor.optimizeBusUtilization();
  }

  @Override
  public void updateInputs(LauncherIOInputs inputs) {
    BaseStatusSignal.refreshAll(motorVelocity, setPointError, rollerPosition);

    inputs.motorVelocityRotationsPerSec = motorVelocity.getValueAsDouble();
    inputs.mechanismVelocityRotationsPerSec = inputs.motorVelocityRotationsPerSec * gearRatio;
    inputs.rollerPosition = rollerPosition.getValueAsDouble();
    // inputs.motorAppliedVolts = motorAppliedVolts.getValueAsDouble();
    // inputs.motorCurrentAmps = motorCurrent.getValueAsDouble();
    // inputs.setPointError = setPointError.getValueAsDouble();
    inputs.setPoint = setPoint;
  }

  @Override
  public void setVelocityRPS(double velocityRPS) {
    setPoint = velocityRPS * gearRatio;

    if (setPoint == 0) {
      voltageOutCommand.withOutput(0.0);
      motor.setControl(voltageOutCommand);
    } else {
      velocityVoltageCommand.withVelocity(velocityRPS).withSlot(0);
      motor.setControl(velocityVoltageCommand);
    }
  }
}
