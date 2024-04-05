package frc.robot.commands.note;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.launcher.Launcher;
import frc.robot.subsystems.pneumatics.SpikeCompressor;

public class Launch extends Command {
  private boolean complete = false;

  private Timer totalTime = new Timer();
  private Timer actionTime = new Timer();

  // subsystems
  private final Intake intake;
  private final Launcher launcher;
  private final SpikeCompressor compressor;

  private enum State {
    SPIN_UP,
    FEED,
    FINISH
  }

  private State state;

  public Launch(Intake intake, Launcher launcher, SpikeCompressor compressor) {
    this.intake = intake;
    this.launcher = launcher;
    this.compressor = compressor;
    state = State.SPIN_UP;
    complete = false;

    addRequirements(intake, launcher, compressor);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    intake.disableIntake();
    launcher.setVelocity(40.0);
    totalTime.restart();
    actionTime.reset();

    compressor.disable();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    compressor.disable();

    switch (state) {
      case SPIN_UP:
        if (launcher.isReadyToShoot()) {
          state = State.FEED;
          actionTime.restart();
        }

        break;
      case FEED:
        intake.setVelocity(30.0);

        if (!launcher.isReadyToShoot()) {
          state = State.FINISH;
          actionTime.restart();
        } else if (actionTime.hasElapsed(1.0)) {
          complete = true;
        }

        break;
      case FINISH:
        if (actionTime.hasElapsed(0.2)) {
          complete = true;
        }

        break;
      default:
        complete = true;
        System.out.println("Incorrectly set state for Launch command");
        break;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // Stop the launcher
    intake.disableIntake();
    launcher.disableLauncher();

    compressor.enable();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return complete || totalTime.hasElapsed(50.0);
  }
}
