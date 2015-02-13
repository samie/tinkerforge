# TinkerForge MQTT Dashboard

Real-time Dashboard application for MQTT messages. The basic setup subscribes messages from TinkerForge sensors.

## Setup

    git clone https://github.com/samie/tinkerforge.git
    cd tinkerforge/
    mvn jetty:run

The subscribed MQTT topics are listed and configured in [Topic.java](https://github.com/samie/tinkerforge/blob/master/src/main/java/org/vaadin/tinkerforge/Topic.java)
