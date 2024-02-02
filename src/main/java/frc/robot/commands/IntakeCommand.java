package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drive.ModuleIOTalonFX;
import frc.robot.subsystems.intake.IntakeIOTalonFX;

public class IntakeCommand extends Command {
  private final IntakeIOTalonFX intake;
  private final ModuleIOTalonFX drive;
  private double targetSpeed = 100d;

  public IntakeCommand(IntakeIOTalonFX intake, ModuleIOTalonFX drive) {
    this.intake = intake;
    this.drive = drive;
  }

  @Override
  public void execute() {
    double currentSpeed = drive.getSelectedSensorVelocity();
    double speedSet = -targetSpeed - currentSpeed;
    intake.setSpeed(speedSet);
  }

  @Override
  public void end(boolean interrupted) {
    intake.setSpeed(0);
  }
}
