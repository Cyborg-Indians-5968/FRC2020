package frc.robot;

public class AutoNavBounce implements IRobotMode {

    private IDrive Drive;
    private double angularSpeed = 0.2;
    private double forwardSpeed = 0.4;

    public AutoNavBounce(IDrive drive) {
        this.drive = drive;
    }

    @Override
    public void init() { 


    @Override
    public void periodic() {
        //Nothing to do 
    }
}