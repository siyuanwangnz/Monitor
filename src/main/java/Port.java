package main.java;

import gnu.io.NRSerialPort;
import gnu.io.PortInUseException;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TooManyListenersException;

public class Port {
    private Port() {
//        java.util.Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new Port.PortListener(), 500, 5000);
    }

    class PortListener extends TimerTask {

        @Override
        public void run() {
            synchronized (Port.class){
                System.out.println("Port Listening ...");

                if (serialPort != null && (!serialPort.isConnected())) {
                    openPort(PortName, PortBaud);
                }
            }
        }
    }

    private static volatile Port instance = null;

    public static Port getInstance() {
        if (instance == null) {
            synchronized (Port.class) {
                if (instance == null) instance = new Port();
            }
        }
        return instance;
    }

    private NRSerialPort serialPort;
    private String PortName = "";
    private int PortBaud = 0;

    //get current port name
    public String getPortName() {
        return serialPort != null ? serialPort.toString() : "NULL COM CONNECT";
    }

    //get port list
    public final Set<String> findPort() {
        return NRSerialPort.getAvailableSerialPorts();
    }

    //open
    public final void openPort(String portName, int baudrate) {

        try {
            PortName = portName;
            PortBaud = baudrate;

            serialPort = new NRSerialPort(portName, baudrate);
            serialPort.connect();

        } catch (Exception e) {
            System.out.println("+++" + e + "+++");
        }
    }

    //close
    public void closePort() {
        if (serialPort != null) {
            serialPort.disconnect();
            serialPort = null;
        }
    }

    //send
    public void sendToPort(byte[] order) {
        if (serialPort == null) {
            return;
        }
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(serialPort.getOutputStream());
            ;
            out.write(order);
            out.flush();

        } catch (IOException e) {
            System.out.println("***" + e + "***");
            serialPort.disconnect();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //read
    public byte[] readFromPort() {
        byte[] bytes = null;
        DataInputStream in = null;
        if (serialPort == null) {
            return bytes;
        }

        try {

            in = new DataInputStream(serialPort.getInputStream());
            int bufflenth = in.available();

            while (bufflenth != 0) {
                bytes = new byte[bufflenth];
                in.read(bytes);
                bufflenth = in.available();
            }
        } catch (IOException e) {
            e.printStackTrace();
            serialPort.disconnect();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    //listener
    public void addListener(DataAvailableListener availableListener) {
        if (serialPort == null) {
            return;
        }
        try {
            serialPort.addEventListener(new SerialListener(availableListener));
            serialPort.notifyOnDataAvailable(true);

        } catch (TooManyListenersException e) {
            e.printStackTrace();
        }
    }

    private class SerialListener implements SerialPortEventListener {
        private DataAvailableListener availableListener;

        public SerialListener(DataAvailableListener availableListener) {
            this.availableListener = availableListener;
        }

        @Override
        public void serialEvent(SerialPortEvent serialPortEvent) {
            switch (serialPortEvent.getEventType()) {

                case SerialPortEvent.BI:
                    System.out.println("Break");
                    break;

                case SerialPortEvent.OE:

                case SerialPortEvent.FE:

                case SerialPortEvent.PE:

                case SerialPortEvent.CD:

                case SerialPortEvent.CTS:

                case SerialPortEvent.DSR:

                case SerialPortEvent.RI:

                case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                    break;

                case SerialPortEvent.DATA_AVAILABLE:
                    if (availableListener != null) {
                        availableListener.dataAvailable();
                    }
                    break;
            }
        }

    }


    public interface DataAvailableListener {
        void dataAvailable();
    }

}
