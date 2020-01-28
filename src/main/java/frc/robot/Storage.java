package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;

public class Storage implements IMotorPeripheral{

    private TalonSRX storageMotor;
    private DigitalInput storageSwitch;
    private IEncoder encoder;

    private double motorSpeed;
    private boolean isAdvancing;

    private static final double HIGH = 0.9;
    private static final double LOW = 0.0;
    private static final double DELAY = 2.0;

    public Storage(){
        storageMotor = new TalonSRX(PortMap.CAN.STORAGE_MOTOR_CONTROLLER);
        storageMotor.setInverted(true);

        storageSwitch = new DigitalInput(0); // TODO: Set actual port

        stop();
    }

    @Override
    public void start(){
        motorSpeed = HIGH;
    }

    @Override
    public void stop(){
        motorSpeed = LOW;
    }

    public void advance(){
        isAdvancing = true;
        start();
        Timer.delay(DELAY);
        stop();
        isAdvancing = false;
    }

    @Override
    public void init(){
        stop();
        isAdvancing = false;
    }

    @Override
    public void periodic(){
        if(!storageSwitch.get()&&!isAdvancing){
            advance();
        }

        storageMotor.set(ControlMode.PercentOutput, motorSpeed);
    }

}
