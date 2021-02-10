package frc.robot;

public class AutoGalacticABlue implements IRobotMode {
    
    private IDrive drive;
    private ILauncher launcher;

    public AutoGalacticABlue(IDrive drive, ILauncher launcher) {
        this.drive = drive;
        this.launcher = launcher;
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