package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Storage implements IMotorPeripheral {

    private CANSparkMax storageMotor;
    private DigitalInput topSwitch;
    private DigitalInput bottomSwitch;
    private IEncoder encoder;
    public StorageMode storageMode;

    private double motorSpeed;
    private int balls = 0;
    private boolean isAdvancing;

    private static final double HIGH = 0.9;
    private static final double REVERSE = -0.9;
    private static final double LOW = 0.0;
    private static final double DELAY = 2.0;
    private static final int MAXBALLS = 5;

    public Storage() {
        storageMotor = new CANSparkMax(PortMap.CAN.STORAGE_MOTOR_CONTROLLER, MotorType.kBrushless);
        storageMotor.setInverted(true);

        bottomSwitch = new DigitalInput(0); // TODO: Set actual port
        topSwitch = new DigitalInput(1); // TODO: Set actual port

        storageMode = StorageMode.COLLECTING;

        stop();
    }

    // sets the motor speed to high
    @Override
    public void start() {
        motorSpeed = HIGH;
    }

    public void backtrack() {
        motorSpeed = REVERSE;
    }

    public void reverse() {
        isAdvancing = true;
        backtrack();
        Timer.delay(DELAY);
        stop();
        isAdvancing = false;
    }

    public void prime() {
        if (storageMode != StorageMode.FIRING) {
            for (int i = 0; i < MAXBALLS - balls; i++) {
                advance();
            }
        }
        storageMode = StorageMode.FIRING;
    }

    public void unprime() {
        if (storageMode != StorageMode.COLLECTING && balls > 0) {
            for (int i = 0; i < MAXBALLS - balls; i++) {
                reverse();
            }
        }
        storageMode = StorageMode.COLLECTING;
    }

    @Override
    public void stop() {
        motorSpeed = LOW;
    }

    // advances the ball to the launcher
    public void advance() {
        isAdvancing = true;
        start();
        Timer.delay(DELAY);
        stop();
        isAdvancing = false;
    }

    // stops the motor and advicing
    @Override

    public void init() {
        storageMode = StorageMode.COLLECTING;
        stop();
        isAdvancing = false;
    }

    // advances the ball and sets the storage motor's speed
    @Override

    public void periodic() {
        if (!bottomSwitch.get() && !isAdvancing) {
            advance();
            balls++;
        } else if (!topSwitch.get()) {
            balls--;
        }
        if (balls == 0) {
            storageMode = StorageMode.COLLECTING;
        } else if (balls == 5) {
            storageMode = StorageMode.FIRING;
        }

        storageMotor.set(motorSpeed);
    }

}
