package frc.robot;

public class AutoAiming implements IRobotMode {
   
    private IDrive drive;
    private ILauncher launcher;
    private ILimelight limelight;
    
    private double offsetTurnLeft = -limelight.getHorizontalOffset();
    private double offsetTurnRight = limelight.getHorizontalOffset();

    public AutoAiming(IDrive drive, ILauncher launcher, ILimelight limelight) {
        this.drive = drive;
        this.launcher = launcher;
        this.limelight = limelight;
    }

    @Override
    public void init() {
        autoTurn();
    
    }

    public void autoTurn() {
        if (limelight.getHorizontalOffset() > 0.01) {
            drive.rotateRelative(offsetTurnLeft, () -> autoRange());
        }
        else if (limelight.getHorizontalOffset() < 0.01) {
            drive.rotateRelative(offsetTurnRight, () -> autoRange());
        }
    }

    public void autoRange() {
        // idk
    }

    /*
    public void launcherShoot() {
        launcher.shoot();
    }

    */

    @Override
    public void periodic() {
        //Nothing to do 
    }
    
}