package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class TalonEncoder implements IEncoder {
    
    private boolean isInverted = false;
    private TalonSRX talon;
    private double distancePerPulse = 1.0;
    private int zeroPoint = 0;

        public TalonEncoder(TalonSRX talon) {
            this.talon = talon;
        }

        @Override
        public void setInverted(boolean inverted) {
            isInverted = inverted;
        }

        @Override
        public void setDistancePerPulse(double distance) {
            distancePerPulse = distance;
        }

        @Override
        public double getDistance() {
            // Convert the raw reading to distance and return it
            return getPosition() * distancePerPulse;
        }

        public double getPosition() {
            int rawEncoderValue = talon.getSensorCollection().getQuadraturePosition() - zeroPoint;
            if (isInverted) {
                rawEncoderValue = -rawEncoderValue;
            }
            return rawEncoderValue;
        }

        @Override
        public void reset() {
            zeroPoint = talon.getSensorCollection().getQuadraturePosition();
    }
}