package frc.robot;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends RobotBase {

    private IRobotMode disabledMode;
    private IRobotMode autonomousMode;
    private IRobotMode teleoperatedMode;

    private IDrive drive;
    private ILauncher launcher;
    private IColorWheel wheel;
    private IGyroscopeSensor gyroscope;
    private ILimelight limelight;

    public Robot() {
        gyroscope = new NavXMXP();
        launcher = new Launcher();
        limelight = new Limelight();
        wheel = new NullColorWheel();

        drive = new Drive(gyroscope);
        //drive = new NullDrive();
        disabledMode = new DisabledMode();
        autonomousMode = new AutonomousMode(drive);
        teleoperatedMode = new TeleoperatedMode(drive, launcher, limelight, wheel);
    }

    @Override
    public void startCompetition() {
        HAL.observeUserProgramStarting();

        IRobotMode currentMode = null;
        IRobotMode desiredMode = null;

        while (true) {
            desiredMode = getDesiredMode();

            if (desiredMode != currentMode) {
                LiveWindow.setEnabled(isTest());
                doPeripheralReinitialization();
                desiredMode.init();
                currentMode = desiredMode;
            }
            currentMode.periodic();
            doPeripheralPeriodicProcessing();
            SmartDashboard.updateValues();
            LiveWindow.updateValues();
        }
    }

    private void doPeripheralReinitialization() {
        drive.init();

        launcher.init();
    }

    private void doPeripheralPeriodicProcessing() {
        drive.periodic();
        
        launcher.periodic();
        limelight.periodic();
        wheel.periodic();
        
        Debug.periodic();
    }

    private IRobotMode getDesiredMode() {
        if (isDisabled()) {
            HAL.observeUserProgramDisabled();
            return disabledMode;
        } else if (isAutonomous()) {
            HAL.observeUserProgramAutonomous();
            return autonomousMode;
        } else if (isOperatorControl()) {
            HAL.observeUserProgramTeleop();
            return teleoperatedMode;
        } else if (isTest()) {
            HAL.observeUserProgramTest();
            return teleoperatedMode;
        } else {
            throw new IllegalStateException("Robot is in an invalid mode");
        }
    }

    @Override
    public void endCompetition() {
        //TODO: add end of competition code here
    }

}