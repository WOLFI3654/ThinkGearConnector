package me.thomas_windt.thinkgearconnector.json;

import java.util.HashMap;

/**
 * Created by root on 19.05.2017.
 */
public class EEGPowerPacket implements  Packet{

    private long delta,theta,lowAlpha,highAlpha,lowBeta,highBeta,lowGamma,highGamma;


    public EEGPowerPacket setDelta(long delta) {
        this.delta = delta;        return this;

    }

    public EEGPowerPacket setTheta(long theta) {
        this.theta = theta;        return this;

    }

    public EEGPowerPacket setLowAlpha(long lowAlpha) {
        this.lowAlpha = lowAlpha;        return this;

    }

    public EEGPowerPacket setHighAlpha(long highAlpha) {
        this.highAlpha = highAlpha;        return this;

    }

    public EEGPowerPacket setLowBeta(long lowBeta) {
        this.lowBeta = lowBeta;        return this;

    }

    public EEGPowerPacket setHighBeta(long highBeta) {
        this.highBeta = highBeta;        return this;

    }

    public EEGPowerPacket setLowGamma(long lowGamma) {
        this.lowGamma = lowGamma;        return this;

    }

    public EEGPowerPacket setHighGamma(long highGamma) {
        this.highGamma = highGamma;
        return this;
    }

    public long getDelta() {
        return delta;
    }

    public long getHighAlpha() {
        return highAlpha;
    }

    public long getHighBeta() {
        return highBeta;
    }

    public long getHighGamma() {
        return highGamma;
    }

    public long getLowAlpha() {
        return lowAlpha;
    }

    public long getLowBeta() {
        return lowBeta;
    }

    public long getLowGamma() {
        return lowGamma;
    }

    public long getTheta() {
        return theta;
    }

    @Override
    public String toString() {
        return "Gamma" +getHighGamma();
    }

    @Override
    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("delta",this.delta);
        map.put("theta",this.theta);
        map.put("lowAlpha",this.lowAlpha);
        map.put("highAlpha",this.highAlpha);
        map.put("lowBeta",this.lowBeta);
        map.put("highBeta",this.highBeta);
        map.put("lowGamma",this.lowGamma);
        map.put("highGamma",this.highGamma);



        return map;
    }
}

