package frc.robot;

public class AutoNavBounce implements IRobotMode {

    private IDrive drive;
    private double speed = 0.4;

    public AutoNavBounce(IDrive drive) {
        this.drive = drive;
    }

    @Override
    public void init() { // Intializes First Step
        driveStart();
    }

    public void driveStart() { // Step 1
        drive.driveDistance(60, speed, 45, () -> driveStraightLong());
    }
    
    public void driveStraightLong() { // Step 2
        drive.driveDistance(120, speed, 162, () -> driveStraightShort());
    }

    public void driveStraightShort() { // Step 3
        drive.driveDistance(12, speed, 90, () -> driveStraightLong1());
    }

    public void driveStraightLong1() { // Step 4
        drive.driveDistance(106, speed, 0, () -> driveStraightLong2());
    }

    public void driveStraightLong2() { // Step 5
        drive.driveDistance(106, speed, 180, () -> driveStraightShort1());
    }

    public void driveStraightShort1() { // Step 6
        drive.driveDistance(40, speed, 90, () -> driveStraightShort3());
    }

    public void driveStraightShort3() { // Step 7
        drive.driveDistance(67, speed, 10, () -> driveStraightEnd());
    }

    public void driveStraightEnd() { // Step 8
        drive.driveDistance(35, speed, 135, null);
    }

    @Override
    public void periodic() {
        //Nothing to do 
    }
}