

package frc.robot;

public interface IDrive {

    public DriveMode getCurrentDriveMode();

    // Turns the robot by a relative angle
    public void rotateDegrees(double angle);

    // Turns the robot to an absolute angle
    public void lookAt(double angle);

    public void driveDistance(double distanceInches, double forwardSpeed, double strafeSpeed);

    /*
     * completionRoutine is called when the current action has been completed
     */
    public void driveDistance(double distanceInches, double forwardSpeed, double strafeSpeed, Runnable completionRoutine);

    public void rotateDegrees(double angle, Runnable completionRoutine);

    public void lookAt(double angle, Runnable completionRoutine);

    /*
     * This is the method used to drive manually during teleoperated mode
     */
    public void driveManual(double forwardSpeed, double strafeSpeed);

    // This turns the robot to an absolute field angle

    public void stop();

    public void init();

    /*
     * Called periodically to actually execute the driving and rotating set by
     * the driveDistance() and rotateDegrees() methods
     */
    public void periodic();

}
