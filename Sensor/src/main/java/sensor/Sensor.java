package sensor;


import org.developers.sensor.client.SensorClient;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Silwest
 */
public class Sensor {
    public static void main(String[] args) {
//        System.setProperty("java.library.path", "C:/sensor/lib");
        new SensorClient().start();
    }
}
