package frc.robot.commands.note;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.amp.Amp;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.launcher.Launcher;

public class AmpScore extends Command {
  private static double LAUNCHER_STARTING_SPEED = 10.0;
  private static double LAUNCHER_POSITIONING_SPEED = 5.0;
  // private static double TARGET_ = 5.0;

  private static double POS_FOR_TIME = 0.5;

  // state variables
  private boolean complete = false;

  private Timer endBuffer = new Timer();
  private Timer positioningTime = new Timer();

  // subsystems
  private final Intake intake;
  private final Launcher launcher;
  private final Amp amp;

  private enum State {
    SPIN_UP,
    LOAD,
    GRAB,
    POSITION,
    SCORE
  }

  private State state;

  public AmpScore(Intake intake, Launcher launcher, Amp amp) {
    this.intake = intake;
    this.launcher = launcher;
    this.amp = amp;

    addRequirements(intake, launcher);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    state = State.SPIN_UP;
    // Reset amp arm position
    amp.reset();

    positioningTime.reset();
    positioningTime.stop();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    switch (this.state) {
      case SPIN_UP:
        intake.disableIntake();
        launcher.setVelocity(LAUNCHER_STARTING_SPEED);

        if (launcher.getVelocityRPS() > LAUNCHER_STARTING_SPEED - 0.5) {
          state = State.LOAD;
          positioningTime.start();
        }
        break;
      case LOAD:
        intake.setVelocity(20.0);
        if (positioningTime.hasElapsed(POS_FOR_TIME)) {
          state = State.GRAB;
          launcher.disableLauncher();
          intake.disableIntake();
          positioningTime.restart();
        }
        break;
      case GRAB:
        amp.closeWrist();
        if (positioningTime.hasElapsed(0.5)) {
          state = State.POSITION;
          positioningTime.restart();
        }
        break;
      case POSITION:
        launcher.setVelocity(LAUNCHER_POSITIONING_SPEED);
        amp.activateElbow();
        if (positioningTime.hasElapsed(0.1)) {
          state = State.SCORE;
          positioningTime.restart();
        }
        break;
      case SCORE:
        amp.openWrist();
        if (positioningTime.hasElapsed(0.1)) {
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
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return complete;
  }
}
