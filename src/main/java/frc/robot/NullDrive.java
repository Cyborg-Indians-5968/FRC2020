

package frc.robot;

public class NullDrive implements IDrive {
    public DriveMode getCurrentDriveMode() {
        return DriveMode.DRIVERCONTROL;
    }

    public void driveDistance(double distanceInches, double xDirectionSpeed, double yDirectionSpeed) {
    }

    public void rotateDegrees(double angle, double angularSpeed) {
    }

    public void driveDistance(double distanceInches, double xDirectionSpeed, double yDirectionSpeed, Runnable completionRoutine) {
    }

    public void rotateDegrees(double angle, double angularSpeed, Runnable completionRoutine) {
    }

    public void driveManual(double xDirectionSpeed, double yDirectionSpeed) {
    }

    public void lookAt(double angle) {
    }

    public void lookAt(double angle, double angularSpeed) {
    }

    public void stop() {
    }

    public void init() {
    }

    public void periodic() {
    }
}
