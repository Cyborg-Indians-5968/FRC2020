

package frc.robot;

public class NullDrive implements IDrive {
    
    public DriveMode getCurrentDriveMode() {
        return DriveMode.IDLEORMANUAL;
    }

    public void rotateRelative(double angle) {
    }

    public void rotateAbsolute(double angle) {
    }

    public void driveDistance(double distanceInches, double speed, double angle) {
    }

    public void driveDistance(double distanceInches, double speed, double angle, Runnable completionRoutine) {  
    }

    public void rotateRelative(double angle, Runnable completionRoutine) {
    }

    public void rotateAbsolute(double angle, Runnable completionRoutine) {
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
