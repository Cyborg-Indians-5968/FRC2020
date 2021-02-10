package frc.robot;

public class AutoNavSlalom implements IRobotMode {

    private IDrive Drive;
    private double forwardSpeed = 0.4;
    private double strafeSpeed = 0.1;
    private double angularSpeed = 0.1;

    public AutoNavSlalom(IDrive drive) {
        this.drive = drive;
    }

    @Override
    public void init() {

    }

    @Override
    public void periodic() {
        //Nothing to do 
    }
}