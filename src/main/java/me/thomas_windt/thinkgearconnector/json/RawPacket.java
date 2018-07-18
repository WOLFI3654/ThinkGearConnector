package me.thomas_windt.thinkgearconnector.json;

import java.util.HashMap;

/**
 * Created by root on 19.05.2017.
 */
public class RawPacket implements Packet {

    private int rawEeg;

    public int getRawEeg() {
        return rawEeg;
    }

    @Override
    public HashMap<String, Object> toHashMap() {
        HashMap<String,Object> map = new HashMap<>();
        map.put("rawEeg",this.rawEeg);
        return map;
    }
}
