package frc.robot;

public class AutoNavSlalom implements IRobotMode {

    private IDrive drive;
    private double speed = 0.4;

    public AutoNavSlalom(IDrive drive) {
        this.drive = drive;
    }

    @Override
    public void init() {
        driveStraightShort();
    }

    private void driveStraightShort() {
        Debug.log("Slalom Step 1 Completed");
        drive.driveDistance(30, speed, 0, () -> driveDiagonalLong());
    }

    private void driveDiagonalLong() {
        Debug.log("Slalom Step 2 Completed");
        drive.driveDistance(85, speed, -45, () -> driveStraightLong());
    }

    private void driveStraightLong() {
        Debug.log("Slalom Step 3 Completed");
        drive.driveDistance(120, speed, 0, () -> driveDiagonalLong1());
    }

    private void driveDiagonalLong1() {
        Debug.log("Slalom Step 4 Completed");
        drive.driveDistance(85, speed, 45, () -> driveDiagonalShort());
    }

    private void driveDiagonalShort() {
        Debug.log("Slalom Step 5 Completed");
        drive.driveDistance(42.5, speed, -45, () -> driveDiagonalShort1());
    }

    private void driveDiagonalShort1() {
        Debug.log("Slalom Step 6 Completed");
        drive.driveDistance(42.5, speed, -135, () -> driveDiagonalLong2());
    }

    private void driveDiagonalLong2() {
        Debug.log("Slalom Step 7 Completed");
        drive.driveDistance(85, speed, 135, () -> driveStraightLong1());
    }

    private void driveStraightLong1() {
        Debug.log("Slalom Step 8 Completed");
        drive.driveDistance(120, speed, 180, () -> driveDiagonalLong3());
    }

    private void driveDiagonalLong3() {
        Debug.log("Slalom Step 9 Completed");
        drive.driveDistance(85, speed, -135, () -> driveStraightShort1());
    }

    private void driveStraightShort1() {
        Debug.log("Slalom Step 10 Completed");
        drive.driveDistance(30, speed, 180, null);
    }
    
    @Override
    public void periodic() {
        //Nothing to do 
    }
}