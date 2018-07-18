package me.thomas_windt.thinkgearconnector.json;

import java.util.HashMap;

/**
 * Created by root on 19.05.2017.
 */
public class StatusPacket implements Packet{
    private String status;
    private int poorSignalLevel;

    public int getPoorSignalLevel() {
        return poorSignalLevel;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("status",this.status);
        map.put("poorSignalLevel",this.poorSignalLevel);
        return map;
    }
}
