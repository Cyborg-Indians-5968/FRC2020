

package frc.robot;

public interface IDrive {

    public DriveMode getCurrentDriveMode();

    public void driveDistance(double distanceInches, double xDirectionSpeed, double yDirectionSpeed);

    // This turns the robot a relative angle
    public void rotateDegrees(double angle, double angularSpeed);

    /*
     * completionRoutine is called when the current action has been completed
     */
    public void driveDistance(double distanceInches, double xDirectionSpeed, double yDirectionSpeed, Runnable completionRoutine);

    public void rotateDegrees(double angle, double angularSpeed, Runnable completionRoutine);

    /*
     * This is the method used to drive manually during teleoperated mode
     */
    public void driveManual(double xDirectionSpeed, double yDirectionSpeed);

    // This turns the robot to an absolute field angle
    public void lookAt(double angle, double angularSpeed);

    public void stop();

    public void init();

    /*
     * Called periodically to actually execute the driving and rotating set by
     * the driveDistance() and rotateDegrees() methods
     */
    public void periodic();

}
