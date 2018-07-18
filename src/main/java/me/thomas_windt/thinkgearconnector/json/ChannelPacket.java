package me.thomas_windt.thinkgearconnector.json;

import java.util.HashMap;

/**
 * Created by root on 19.05.2017.
 */
public class ChannelPacket implements Packet{

    private ESensePacket eSense;
    private EEGPowerPacket eegPower;
    private int poorSignalLevel;


    public ChannelPacket setEegPower(EEGPowerPacket eegPower) {
        this.eegPower = eegPower;
        return this;
    }

    public ChannelPacket seteSense(ESensePacket eSense) {
        this.eSense = eSense;
        return this;
    }

    public ChannelPacket setPoorSignalLevel(int poorSignalLevel) {
        this.poorSignalLevel = poorSignalLevel;
        return this;
    }

    public EEGPowerPacket getEegPower() {
        return eegPower;
    }

    public ESensePacket geteSense() {
        return eSense;
    }

    public int getPoorSignalLevel() {
        return poorSignalLevel;
    }

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
