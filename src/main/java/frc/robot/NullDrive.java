

package frc.robot;

public class NullDrive implements IDrive {
    
    public DriveMode getCurrentDriveMode() {
        return DriveMode.DRIVERCONTROL;
    }

    public void rotateDegrees(double angle) {
    }

    public void lookAt(double angle) {
    }

    public void driveDistance(double distanceInches, double xDirectionSpeed, double yDirectionSpeed) {
    }

    public void driveDistance(double distanceInches, double xDirectionSpeed, double yDirectionSpeed, Runnable completionRoutine) {
    }

    public void rotateDegrees(double angle, Runnable completionRoutine) {
    }

    public void lookAt(double angle, Runnable completionRoutine) {
    }

    public void driveManual(double xDirectionSpeed, double yDirectionSpeed) {
    }

    public void stop() {
    }

    public void init() {
    }

    public void periodic() {
    }
}
