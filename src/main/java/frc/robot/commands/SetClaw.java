package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Claw;


public class SetClaw extends CommandBase {
    private final Claw m_claw;
    private final double m_percent;

    public SetClaw(Claw givenClaw, double percent, double time) {
        m_claw = givenClaw;
        m_percent = percent;

        addRequirements(m_claw); 
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }
    
    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        m_claw.percentClaw(m_percent);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return true;
    }
}
