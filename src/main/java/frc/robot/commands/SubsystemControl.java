// Copyright 2021-2024 FRC 6328
// http://github.com/Mechanical-Advantage
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// version 3 as published by the Free Software Foundation or
// available in the root directory of this project.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.

package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.vision.Vision;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class SubsystemControl {
  private static final double DEADBAND = 0.1;

  // intake constants
  private static final double MAX_INTAKE_SPEED_RPS = 20.0;

  private SubsystemControl() {}

  /**
   * Field relative drive command using two joysticks (controlling linear and angular velocities).
   */
  public static Command joystickDrive(
      Drive drive,
      DoubleSupplier xSupplier,
      DoubleSupplier ySupplier,
      DoubleSupplier omegaSupplier) {
    return Commands.run(
        () -> {
          // Apply deadband
          double linearMagnitude = Math.hypot(xSupplier.getAsDouble(), ySupplier.getAsDouble());
          Rotation2d linearDirection =
              new Rotation2d(xSupplier.getAsDouble(), ySupplier.getAsDouble());
          double omega = omegaSupplier.getAsDouble();

          // Square values
          linearMagnitude = linearMagnitude * linearMagnitude;
          omega = Math.copySign(omega * omega, omega);

          // Calcaulate new linear velocity
          Translation2d linearVelocity =
              new Pose2d(new Translation2d(), linearDirection)
                  .transformBy(new Transform2d(linearMagnitude, 0.0, new Rotation2d()))
                  .getTranslation();

          // Convert to field relative speeds & send command
          drive.runVelocity(
              ChassisSpeeds.fromFieldRelativeSpeeds(
                  linearVelocity.getX() * drive.getMaxLinearSpeedMetersPerSec(),
                  linearVelocity.getY() * drive.getMaxLinearSpeedMetersPerSec(),
                  omega * drive.getMaxAngularSpeedRadPerSec(),
                  drive.getRotation()));
        },
        drive);
  }

  // Simulate a car with front wheel drive
  // Gas is right trigger, brake is left trigger
  // Steering is left joystick x axis or right joystick x axis
  public static Command kartDrive(
      Drive drive,
      DoubleSupplier gasSupplier,
      DoubleSupplier brakeSupplier,
      DoubleSupplier steeringSupplier,
      BooleanSupplier reverseGear) {
    return Commands.run(
        () -> {
          double gas = gasSupplier.getAsDouble();
          double brake = brakeSupplier.getAsDouble();
          double steering = steeringSupplier.getAsDouble();
          double speed = gas - brake;
          if (reverseGear.getAsBoolean()) {
            speed *= -1;
          }
          boolean brakeMotors = (brake >= 0.75 ? false : true);
          drive.runFrontWheelDrive(
              speed * drive.getMaxLinearSpeedMetersPerSec(), steering, brakeMotors, 45.0);
        },
        drive);
  }

  public static Command limelightDrive(
      Drive drive,
      Vision vision,
      DoubleSupplier xSupplier,
      DoubleSupplier ySupplier,
      DoubleSupplier omegaSupplier) {

    return Commands.run(
        () -> {
          // Apply deadband
          double linearMagnitude =
              MathUtil.applyDeadband(
                  Math.hypot(xSupplier.getAsDouble(), ySupplier.getAsDouble()), DEADBAND);
          Rotation2d linearDirection =
              new Rotation2d(xSupplier.getAsDouble(), ySupplier.getAsDouble());
          double omega = MathUtil.applyDeadband(omegaSupplier.getAsDouble(), DEADBAND);

          // Square values
          linearMagnitude = linearMagnitude * linearMagnitude;
          omega = Math.copySign(omega * omega, omega);

          // Calcaulate new linear velocity
          Translation2d linearVelocity =
              new Pose2d(new Translation2d(), linearDirection)
                  .transformBy(new Transform2d(linearMagnitude, 0.0, new Rotation2d()))
                  .getTranslation();

          if (omega != 0.0d) { // Check if the driver isnt trying to turn
            vision.resetError();
          } else if ((omega == 0.0) && (vision.seesTarget())) {
            // Get tX from the vision subsystem. tX is "demand"
            omega = -vision.getDesiredAngle();
          }

          drive.runVelocity(
              // Convert to field relative speeds & send command
              ChassisSpeeds.fromFieldRelativeSpeeds(
                  linearVelocity.getX() * drive.getMaxLinearSpeedMetersPerSec(),
                  linearVelocity.getY() * drive.getMaxLinearSpeedMetersPerSec(),
                  omega * drive.getMaxAngularSpeedRadPerSec(),
                  drive.getRotation()));
        },
        drive);
  }

  public static Command joystickIntake(Intake intake, DoubleSupplier intakeSpeed) {
    return Commands.run(
        () -> {
          double deadbandedSpeed = intakeSpeed.getAsDouble();
          deadbandedSpeed = MathUtil.clamp(deadbandedSpeed, -1.0, 1.0);

          intake.setVelocity(deadbandedSpeed * MAX_INTAKE_SPEED_RPS);
        },
        intake);
  }
}
