package frc.robot.commands.note;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.lib.SpikeController;
import frc.robot.subsystems.amp.Amp;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.launcher.Launcher;
import frc.robot.subsystems.pneumatics.SpikeCompressor;

public class AmpScore extends Command {
  private static double LAUNCHER_STARTING_SPEED = 10.0;
  private static double LAUNCHER_POSITIONING_SPEED = 5.0;

  private static double POS_ROTATIONS = 1.5;

  private double startPosition = 0.0;

  // state variables
  private boolean complete = false;

  private Timer positioningTime = new Timer();

  // subsystems
  private final Intake intake;
  private final Launcher launcher;
  private final Amp amp;
  private final SpikeCompressor compressor;

  private SpikeController operatorController;

  private enum State {
    SPIN_UP,
    FEED,
    LOAD,
    GRAB,
    POSITION,
    SCORE
  }

  private State state;

  public AmpScore(
      Intake intake,
      Launcher launcher,
      Amp amp,
      SpikeController operatorController,
      SpikeCompressor compressor) {
    this.intake = intake;
    this.launcher = launcher;
    this.amp = amp;
    this.operatorController = operatorController;
    this.compressor = compressor;

    addRequirements(intake, launcher);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    state = State.SPIN_UP;
    // Reset amp arm position
    amp.reset();
    compressor.disable();

    positioningTime.reset();
    positioningTime.stop();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    compressor.disable();

    switch (this.state) {
      case SPIN_UP:
        intake.disableIntake();
        launcher.setVelocity(LAUNCHER_STARTING_SPEED);

        if (launcher.getVelocityRPS() > LAUNCHER_STARTING_SPEED - 0.5) {
          state = State.FEED;
          positioningTime.start();
        }
        break;
      case FEED:
        intake.setVelocity(20.0);
        // if the note doesnt enter the launcher within 1 second then we end the command
        if (positioningTime.hasElapsed(1.0)) {
          complete = true;
          break;
        }

        // wait for the note to enter the launcher
        if (launcher.getVelocityRPS() < LAUNCHER_STARTING_SPEED - 0.5) {
          state = State.LOAD;
          intake.disableIntake();
          startPosition = launcher.getPosition();
          positioningTime.restart();
          launcher.disableLauncher();
        }
        break;
      case LOAD:
        if (positioningTime.hasElapsed(1.75)) {
          launcher.disableLauncher();
          positioningTime.restart();
          state = State.GRAB;
          break;
        }

        launcher.setVelocity(6.5);
        // run the launcher until it has spun at least 1.5 rotations
        if ((launcher.getPosition() - startPosition) > 0.9) {
          launcher.disableLauncher();
          positioningTime.restart();
          state = State.GRAB;
        }
        break;
      case GRAB:
        amp.closeWrist();
        if (positioningTime.hasElapsed(0.2)) {
          state = State.POSITION;
          positioningTime.restart();
        }
        break;
      case POSITION:
        launcher.setVelocity(12.0);
        amp.activateElbow();
        if (!operatorController.a().getAsBoolean()) {
          state = State.SCORE;
          positioningTime.restart();
          launcher.disableLauncher();
          amp.openWrist();
        }
        break;
      case SCORE:
        if (positioningTime.hasElapsed(0.2)) {
          amp.deactivateElbow();

          complete = true;
        }
        break;
      default:
        complete = true;
        break;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // Stop the launcher
    intake.disableIntake();
    launcher.disableLauncher();
    amp.reset();
    compressor.enable();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return complete;
  }
}
