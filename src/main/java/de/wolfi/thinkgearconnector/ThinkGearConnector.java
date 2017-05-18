package de.wolfi.thinkgearconnector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jdk.nashorn.internal.parser.JSONParser;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by root on 16.05.2017.
 */
public class ThinkGearConnector {

    private Gson gson;
    class StreamThread extends Thread {
        Scanner reader;
        OutputStreamWriter writer;
        private Socket socket;
        boolean running = false;
        private StreamThread(Socket socket) {
            try {
                this.socket = socket;
                reader = new Scanner((socket.getInputStream()));
                writer = new OutputStreamWriter(socket.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            this.running = true;
            while(this.running){
                if(socket.isClosed()){
                    try {
                        throw new InvalidObjectException("Socket is closed");
                    } catch (InvalidObjectException e) {
                        e.printStackTrace();
                    }
                }



                if(reader.hasNextLine()){
                    String in = reader.nextLine();

                    System.out.println(in);
                }
            }
            try {
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void writeJson(String json){
            Logger.getAnonymousLogger().log(Level.INFO,String.format("Writing: %s",json));
            try {
                writer.write(json);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void close(){
            this.running = false;
        }


    }

    private String appName, sha_1;
    private StreamThread stream;
    public ThinkGearConnector(String appName, String SHA_1) {
        this.appName = appName;
        this.sha_1 = SHA_1;
        gson = new Gson();
    }

    public void open() throws IOException {
        this.stream = new StreamThread(new Socket("127.0.0.1",13854));
        this.stream.start();
    }

    public void auth(){
        this.stream.writeJson(String.format("{\"appName\":\"%s\",\"appKey\":\"%s\"}\n",this.appName,this.sha_1));
    }

    public void switchOutput(boolean enableRawOutput, String format){
        this.stream.writeJson(String.format("{\"enableRawOutput\":%s,\"format\":\"%s\"}\n",enableRawOutput,format));
    }

    public void close(){
        this.stream.close();
    }
}
