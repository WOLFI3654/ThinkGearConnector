package me.thomas_windt.thinkgearconnector.json;

import java.util.HashMap;

/**
 * Created by root on 19.05.2017.
 */
public class ESensePacket implements Packet{

    private int attention;
    private int meditation;


    public ESensePacket setAttention(int attention) {
        this.attention = attention;
        return this;
    }

    public ESensePacket setMeditation(int meditation) {
        this.meditation = meditation;
        return this;
    }

    public int getAttention() {
        return attention;
    }

    public int getMeditation() {
        return meditation;
    }

    @Override
    public String toString() {
        return "Att"+getAttention();
    }

    @Override
    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("attention",this.attention);
        map.put("meditation",this.meditation);
        return map;
    }
}
