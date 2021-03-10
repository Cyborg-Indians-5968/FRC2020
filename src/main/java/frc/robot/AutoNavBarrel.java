package frc.robot;

public class AutoNavBarrel implements IRobotMode {

    private IDrive drive;
    private double speed = 0.8;

    public AutoNavBarrel(IDrive drive) {
        this.drive = drive;
    }

    public void init() { 
        driveStraightLong();
    }

    public void driveStraightLong() { // Step 1
        drive.driveDistance(120, speed, 0, () -> driveStraightShort());
    }

    public void driveStraightShort() { // Step 2
        drive.driveDistance(30, speed, 90, () -> driveStraightShort1());
    }

    public void driveStraightShort1() { // Step 3
        drive.driveDistance(30, speed, 180, () -> driveStraightShort2());
    }

    public void driveStraightShort2() { // Step 4
        drive.driveDistance(30, speed, -90, () -> driveStraightLong1());
    }

    public void driveStraightLong1() { // Step 5
        drive.driveDistance(124, speed, 0, () -> driveStraightShort3());
    }

    public void driveStraightShort3() { // Step 6
        drive.driveDistance(60, speed, -90, () -> driveStraightShort4());
    }

    public void driveStraightShort4() { // Step 7
        drive.driveDistance(30, speed, 180, () -> driveStraightLong2());
    }

    public void driveStraightLong2() { // Step 8
        drive.driveDistance(90, speed, 90, () -> driveStraightLong3());
    }

    public void driveStraightLong3() { // Step 9
        drive.driveDistance(90, speed, 0, () -> driveStraightShort5());
    }

    public void driveStraightShort5() { // Step 10
        drive.driveDistance(30, speed, -90, () -> driveStraightLong4());
    }

    public void driveStraightLong4() { // Step 11
        drive.driveDistance(315, speed, 180, null);
    }
   
    @Override
    public void periodic() {
        //Nothing to do 
    }


}