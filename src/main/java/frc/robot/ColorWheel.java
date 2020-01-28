package frc.robot;

import java.util.Arrays;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.util.Color;

public class ColorWheel implements IColorWheel {

    private TalonSRX wheelMotor;
    private ColorSensorV3 colorSensor;
    private double motorSpeed;

    private static final double HIGH = 0.9;
    private static final double LOW = 0.0;
    private static final int REVOLUTIONS = 4;

    private char[] sequence = { 'R', 'G', 'B', 'Y' };
    private char sensorColor;
    private char desiredColor;
    private char lastColor;
    private int currentRevolutions = 0;

    public ColorWheel() {
        wheelMotor = new TalonSRX(PortMap.CAN.WHEEL_MOTOR_CONTROLLER);
        // wheelMotor.setInverted(true);
        colorSensor = new ColorSensorV3(Port.kOnboard);
        motorSpeed = LOW;
    }

    @Override
    public void spinToColor(char color) {
        desiredColor = color;

    }

    @Override
    public void spinRevolutions() {
        desiredColor = getColor();
    }

    private char getColor(){
        Color color = colorSensor.getColor();
        if (color.equals(Color.kRed)){ 
            return 'R'; 
        }
        else if (color.equals(Color.kLime)){ 
            return 'G';
        }
        else if (color.equals(Color.kCyan)){ 
            return 'B';
        }
        else if (color.equals(Color.kYellow)){
            return 'Y';
        }
        else {
            return 'X';
        }

    };

    @Override
    public void periodic() {
        sensorColor = getColor();

        if (sensorColor == desiredColor && lastColor != sensorColor) {
            currentRevolutions++;
        }

        if (sensorColor == sequence[(new String(sequence).indexOf(desiredColor) + 2) % 4]) {
            motorSpeed = HIGH;
        } else if (currentRevolutions != REVOLUTIONS) {
            motorSpeed = HIGH;
        } else {
            motorSpeed = LOW;
        }

        wheelMotor.set(ControlMode.PercentOutput, motorSpeed);

        lastColor = sensorColor;
    }

}
