package frc.robot;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class TalonEncoder implements IEncoder {
    private boolean isInverted = false;
    private TalonSRX talon;
    private double distancePerPulse = 1.0;
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
            int rawEncoderValue = talon.getSensorCollection().getQuadraturePosition();
            if (isInverted) {
                rawEncoderValue = -rawEncoderValue;
            }
            return rawEncoderValue;
        }

        @Override
        public void reset() {
        ErrorCode error = talon.getSensorCollection().setQuadraturePosition(0, 500);
        if (error != ErrorCode.OK) {
            // Bleh, overly generic exception.
            // If we were feeling fancy, we'd determine an ideal exception
            // based on the error code.
            throw new RuntimeException("Failed to reset Talon encoder! Error code = " + error.toString());
        }
    }
}