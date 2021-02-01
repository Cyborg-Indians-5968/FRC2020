package frc.robot;

public class AutoNav implements IRobotMode {

    private IDrive drive;

    public AutoNav(IDrive drive) {
        this.drive = drive;
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void periodic() {
        //Nothing to do 
    }

}