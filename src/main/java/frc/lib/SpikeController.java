package frc.lib;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class SpikeController extends CommandXboxController {
  private double deadband;

  public SpikeController(int port, double deadband) {
    super(port);
    this.deadband = deadband;
  }

  public void setRumble(double rumbleAmount) {
    this.getHID().setRumble(RumbleType.kBothRumble, rumbleAmount);
  }

  private double deadband(double input) {
    return MathUtil.applyDeadband(input, deadband);
  }

  @Override
  public double getLeftX() {
    return deadband(super.getLeftX());
  }

  @Override
  public double getLeftY() {
    return deadband(super.getLeftY());
  }

  @Override
  public double getRightX() {
    return deadband(super.getRightX());
  }

  @Override
  public double getRightY() {
    return deadband(super.getRightY());
  }

  @Override
  public double getLeftTriggerAxis() {
    return deadband(super.getLeftTriggerAxis());
  }

  @Override
  public double getRightTriggerAxis() {
    return deadband(super.getRightTriggerAxis());
  }
}
