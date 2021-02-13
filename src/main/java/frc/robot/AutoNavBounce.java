package frc.robot;

public class AutoNavBounce implements IRobotMode {

    private IDrive Drive;
    private double speed = 0.4;

    public AutoNavBounce(IDrive drive) {
        this.drive = drive;
    }

    @Override
    public void init() { 
        driveStart();
    }

    @Override
    private void driveStart() {
        // TBD
    }

    @Override
    public void periodic() {
        //Nothing to do 
    }
}