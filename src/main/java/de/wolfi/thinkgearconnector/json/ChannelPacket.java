package de.wolfi.thinkgearconnector.json;

import java.util.HashMap;

/**
 * Created by root on 19.05.2017.
 */
public class ChannelPacket implements Packet{

    private ESensePacket eSense;
    private EEGPowerPacket eegPower;
    private int poorSignalLevel;

    @Override
    public String toString() {
        return eSense.toString() + eegPower.toString() + poorSignalLevel;
    }

    @Override
    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("eSense",this.eSense);
        map.put("eegPower",this.eegPower);
        map.put("poorSignalLevel",this.poorSignalLevel);
        return map;
    }
}
