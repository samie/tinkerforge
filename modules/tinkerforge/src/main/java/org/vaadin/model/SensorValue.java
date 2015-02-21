package org.vaadin.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by sven on 19.02.15.
 */
public class SensorValue {

    private LocalDateTime localDateTime;
    private int rawValue;
    private String deviceType; //TODO not typeSave

    private SensorValue(Builder builder) {
        setLocalDateTime(builder.localDateTime);
        setRawValue(builder.rawValue);
        setDeviceType(builder.deviceType);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(SensorValue copy) {
        Builder builder = new Builder();
        builder.localDateTime = copy.localDateTime;
        builder.rawValue = copy.rawValue;
        builder.deviceType = copy.deviceType;
        return builder;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SensorValue)) return false;
        SensorValue that = (SensorValue) o;
        return Objects.equals(rawValue, that.rawValue) &&
                Objects.equals(localDateTime, that.localDateTime) &&
                Objects.equals(deviceType, that.deviceType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(localDateTime, rawValue, deviceType);
    }

    @Override
    public String toString() {
        return "SensorValue{" +
                "deviceType='" + deviceType + '\'' +
                ", localDateTime=" + localDateTime +
                ", rawValue=" + rawValue +
                '}';
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public int getRawValue() {
        return rawValue;
    }

    public void setRawValue(int rawValue) {
        this.rawValue = rawValue;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public static final class Builder {
        private LocalDateTime localDateTime;
        private int rawValue;
        private String deviceType;

        private Builder() {
        }

        public Builder localDateTime(LocalDateTime localDateTime) {
            this.localDateTime = localDateTime;
            return this;
        }

        public Builder rawValue(int rawValue) {
            this.rawValue = rawValue;
            return this;
        }

        public Builder deviceType(String deviceType) {
            this.deviceType = deviceType;
            return this;
        }

        public SensorValue build() {

            if (localDateTime == null){ throw new NullPointerException("localDateTime is null");}
            if (deviceType == null){ throw new NullPointerException("localDateTime is null");}

            return new SensorValue(this);
        }
    }
}
