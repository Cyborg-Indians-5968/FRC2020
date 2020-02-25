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
    private IMotorPeripheral roller;
    private IMotorPeripheral launcher;
    private Storage storage;
    private IColorWheel wheel;
    private IGyroscopeSensor gyroscope;
    private Endgame endgame;

    public Robot() {
        gyroscope = new NavXMXP();
        roller = new Roller();
        launcher = new Launcher();
        storage = new Storage();
        endgame = new Endgame();

        drive = new Drive(gyroscope);
        disabledMode = new DisabledMode();
        autonomousMode = new LineAuto(drive);
        teleoperatedMode = new TeleoperatedMode(drive, roller, launcher, storage, wheel, endgame);
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

        roller.init();
        launcher.init();
        storage.init();
        endgame.init();
    }

    private void doPeripheralPeriodicProcessing() {
        drive.periodic();
        
        roller.periodic();
        launcher.periodic();
        storage.periodic();
        wheel.periodic();
        endgame.periodic();
        
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