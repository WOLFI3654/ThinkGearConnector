package me.thomas_windt.thinkgearconnector;

import com.google.gson.Gson;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import me.thomas_windt.thinkgearconnector.json.*;

import javax.microedition.io.StreamConnection;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InvalidObjectException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by root on 16.05.2017.
 */
public class ThinkGearConnector {


    private String address;
    private boolean debug = false;
    private Gson gson;
    private String appName, sha_1, host;
    private int port = 13854;
    private StreamThread stream;
    private CommPortIdentifier cport;
    private boolean hacked = false;

    public ThinkGearConnector(CommPortIdentifier port) {
        this.hacked = true;
        this.cport = port;
        gson = new Gson();

    }

    public ThinkGearConnector(String appName, String SHA_1) {
        this.appName = appName;
        this.sha_1 = SHA_1;
        this.host = "127.0.0.1";
        gson = new Gson();
    }

    public ThinkGearConnector(String address) {
        this.address = address;
        gson = new Gson();
    }

    public void setHost(String host) {
        this.setHost(host, this.port);
    }

    public void setHost(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void open() throws IOException, PortInUseException {
        if (!hacked)
            this.stream = new StreamThread(new Socket(this.host, this.port));
        else
            this.stream = new StreamThread(cport.open("ThinkGaerConnectorBridge", 1000));


        this.stream.start();

    }

    public void enableDebug() {
        debug = true;
    }

    public void auth() {
        this.stream.writeJson(String.format("{\"appName\":\"%s\",\"appKey\":\"%s\"}\n", this.appName, this.sha_1));
    }

    public void switchOutput(boolean enableRawOutput, String format) {
        this.stream.writeJson(String.format("{\"enableRawOutput\":%s,\"format\":\"%s\"}\n", enableRawOutput, format));
    }

    public void registerEventHandler(EventListener e) {
        this.stream.listeners.add(e);
    }

    public void close() {
        this.stream.close();
    }

    public interface EventListener {
        public void processPacket(Packet in);
    }

    class StreamThread extends Thread {
        Scanner reader;
        OutputStreamWriter writer;
        boolean running = false;
        private StreamConnection stream;
        private CommPort port;
        private List<EventListener> listeners = new ArrayList<>();
        private Socket socket;

        private StreamThread(StreamConnection stream) {
            this.stream = stream;
            try {
                reader = new Scanner(new InputStreamReader(stream.openInputStream()));
                writer = new OutputStreamWriter(stream.openOutputStream());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private StreamThread(CommPort port) {
            this.port = port;
            try {
                reader = new Scanner(new InputStreamReader(port.getInputStream()));
                writer = new OutputStreamWriter(port.getOutputStream());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private StreamThread(Socket socket) {
            try {
                this.socket = socket;
                reader = new Scanner(new InputStreamReader(socket.getInputStream()));
                writer = new OutputStreamWriter(socket.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            this.running = true;
            if (!hacked) {
                while (this.running) {
                    if (socket.isClosed()) {
                        try {
                            throw new InvalidObjectException("Socket is closed");
                        } catch (InvalidObjectException e) {
                            e.printStackTrace();
                        }
                    }


                    try {
                        if (reader.hasNextLine()) {
                            String in = reader.nextLine();
                            if (in.isEmpty()) continue;
                            if (debug) System.out.println(in);
                            Class<? extends Packet> clazz = null;
                            if (in.contains("status")) clazz = StatusPacket.class;
                            else if (in.contains("eSense")) clazz = ChannelPacket.class;
                            else if (in.contains("blink")) clazz = BlinkPacket.class;
                            else if (in.contains("raw")) clazz = RawPacket.class;

                            Packet packet = gson.fromJson(in, clazz);
                            listeners.forEach((p) -> p.processPacket(packet));

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                }
            } else if (address != null) {
                while (this.running) {
                    if (this.reader.hasNextByte()) {
                        if (this.reader.nextByte() == 170)
                            if (this.reader.nextByte() == 170) {
                                boolean bigPacket = false;

                                byte payloadLength = reader.nextByte();
                                if (payloadLength > 169)                      //Payload length can not be greater than 169
                                    return;

                                byte payloadData[] = new byte[64];
                                byte generatedChecksum = 0;
                                for (int i = 0; i < payloadLength; i++) {
                                    payloadData[i] = this.reader.nextByte();            //Read payload into memory
                                    generatedChecksum += payloadData[i];
                                }

                                byte checksum = this.reader.nextByte();                      //Read checksum byte from stream
                                generatedChecksum = (byte) (255 - generatedChecksum);   //Take one's compliment of generated checksum

                                if (checksum == generatedChecksum) {

                                    int poorQuality = 200;
                                    int attention = 0;
                                    int meditation = 0;

                                    for (int i = 0; i < payloadLength; i++) {    // Parse the payload

                                        switch (payloadData[i]) {
                                            case (byte)1:
                                                i++;

                                                break;
                                            case (byte)2:
                                                i++;
                                                poorQuality = payloadData[i];
                                                bigPacket = true;
                                                break;
                                            case (byte)4:
                                                i++;
                                                attention = payloadData[i];
                                                break;
                                            case (byte)5:
                                                i++;
                                                meditation = payloadData[i];
                                                break;
                                            case (byte) 0x80:
                                                i = i + 3;
                                                break;
                                            case (byte) 131:
                                                i = i + 25;
                                                break;
                                            default:
                                                break;
                                        } // switch
                                    } // for loop
                                }
                            }
                    }

                }
            } else while (this.running) {
                try {
                    if (reader.hasNextLine()) {
                        String in = reader.nextLine();
                        if (in.isEmpty()) continue;
                        if (debug) System.out.println(in);
                        String[] data = in.split(",");
                        ESensePacket sense = new ESensePacket();
                        sense.setAttention(Integer.valueOf(data[1])).setMeditation(Integer.valueOf(data[2]));
                        EEGPowerPacket power = new EEGPowerPacket();
                        power.setDelta(Long.valueOf(data[3])).setTheta(Long.parseLong(data[4])).setLowAlpha(Long.parseLong(data[5])).setHighAlpha(Long.parseLong(data[6])).setLowBeta(Long.parseLong(data[7])).setHighBeta(Long.parseLong(data[8])).setLowGamma(Long.parseLong(data[9])).setHighGamma(Long.parseLong(data[10]));
                        listeners.forEach((p) -> new ChannelPacket().setEegPower(power).seteSense(sense).setPoorSignalLevel(Integer.parseInt(data[0])));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
            try {
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void writeJson(String json) {
            Logger.getAnonymousLogger().log(Level.INFO, String.format("Writing: %s", json));
            try {
                writer.write(json);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void close() {
            this.running = false;
        }


    }
}
