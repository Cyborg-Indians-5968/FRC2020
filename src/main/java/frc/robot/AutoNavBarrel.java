package frc.robot;

public class AutoNavBarrel implements IRobotMode {

    private IDrive drive;
    private double speed = 0.8;

    public AutoNavBarrel(IDrive drive) {
        this.drive = drive;
    }

    @Override
    public void init() { 
        driveStraightLong();
    }

    public void driveStraightLong() { // Step 1
        drive.driveDistance(120, speed, 90, () -> driveStraightShort());
    }

    public void driveStraightShort() { // Step 2
        drive.driveDistance(30, speed, 90, () -> driveStraightShort1());
    }

    public void driveStraightShort1() { // Step 3
        drive.driveDistance(30, speed, 90, () -> driveStraightShort2());
    }

    private void driveStraightShort2() { // Step 4
        drive.driveDistance(30, speed, 90, () -> driveStraightLong1());
    }

    private void driveStraightLong1() { // Step 5
        drive.driveDistance(124, speed, -90, () -> driveStraightShort3());
    }

    private void driveStraightShort3() { // Step 6
        drive.driveDistance(60, speed, -90, () -> driveStraightShort4());
    }

    private void driveStraightShort4() { // Step 7
        drive.driveDistance(30, speed, -90, () -> driveStraightLong2());
    }

    private void driveStraightLong2() { // Step 8
        drive.driveDistance(90, speed, -90, () -> driveStraightLong3());
    }

    private void driveStraightLong3() { // Step 9
        drive.driveDistance(90, speed, -90, () -> driveStraightShort5());
    }

    private void driveStraightShort5() { // Step 10
        drive.driveDistance(30, speed, -90, () -> driveStraightLong4());
    }

    private void driveStraightLong4() { // Step 11
        drive.driveDistance(315, speed, 0, null);
    }
   
    @Override
    public void periodic() {
        //Nothing to do 
    }


}