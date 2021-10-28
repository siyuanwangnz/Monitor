package main.java;
import gnu.io.NRSerialPort;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.Timer;


public class NRJavaSerialDemo {
    public static void main(String[] args) throws IOException {
        String port = "";
        for(String s:NRSerialPort.getAvailableSerialPorts()){
            System.out.println("Availible port: "+s);
            port=s;
        }

        int baudRate = 115200;
        NRSerialPort serial = new NRSerialPort(port, baudRate);
        System.out.println("Serial Connected");
        serial.connect();

        new Thread(){
            @Override
            public void run(){
                DataInputStream ins = new DataInputStream(serial.getInputStream());
                try{
                    //while(ins.available()==0 && !Thread.interrupted());// wait for a byte
                    while(!Thread.interrupted()) {// read all bytes
                        if(ins.available()>0) {
                            char b = (char)ins.read();
                            //outs.write((byte)b);
                            System.out.print(b);
                        }
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }.start();

        new SendProcess(serial);

        DataOutputStream outs = new DataOutputStream(serial.getOutputStream());
        while(true){
            Scanner scanner = new Scanner(System.in);
            String tmp = scanner.nextLine();

            switch(tmp){
                case "info":
                    try{
                        String info = "get 0x00001\r\n";
                        outs.write(info.getBytes());
                        outs.flush();
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    break;
                default:
                    try{
                        tmp = tmp + "\r\n";
                        outs.write(tmp.getBytes());
                        outs.flush();
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    break;
            }

        }

    }

     static class SendProcess{
        NRSerialPort serial;
        public SendProcess(NRSerialPort serial) {
            this.serial = serial;
            java.util.Timer timer = new Timer();
            timer.scheduleAtFixedRate(new SendProcess.SendTask(), 500, 2000);
        }

         class SendTask extends TimerTask {

            @Override
            public void run() {
                DataOutputStream outs = new DataOutputStream(serial.getOutputStream());
                try{
                    String tmp = "get 0x00001\r\n";
                    outs.write(tmp.getBytes());
                    outs.flush();
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    }





}
