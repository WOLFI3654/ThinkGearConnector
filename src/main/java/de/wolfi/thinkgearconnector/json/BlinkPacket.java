package de.wolfi.thinkgearconnector.json;

import java.util.HashMap;

/**
 * Created by root on 19.05.2017.
 */
public class BlinkPacket implements Packet{
    private int blinkStrength;

    public int getStrength() {
        return blinkStrength;
    }

    @Override
    public String toString() {
        return "Blink: "+ getStrength();
    }

    @Override
    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("blinkStrength",this.blinkStrength);
        return map;
    }
}
