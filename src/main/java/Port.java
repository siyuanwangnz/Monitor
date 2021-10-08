package main.java;

import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TooManyListenersException;

public class Port {
    private Port() {

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

    private SerialPort serialPort;

    //get current port name
    public String getPortName() {
        return serialPort != null ? serialPort.getName() : "NULL COM CONNECT";
    }

    //get port list
    public final ArrayList<String> findPort() {
        Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();

        ArrayList<String> portNameList = new ArrayList<>();

        while (portList.hasMoreElements()) {
            String portName = portList.nextElement().getName();
            portNameList.add(portName);
        }

        return portNameList;
    }

    //open
    public final void openPort(String portName, int baudrate) {

        try {
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);

            CommPort commPort = null;
            try {
                commPort = portIdentifier.open(portName, 2000);
            } catch (PortInUseException e) {
                e.printStackTrace();
            }

            if (commPort instanceof SerialPort) {

                serialPort = (SerialPort) commPort;

                try {
                    serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                } catch (UnsupportedCommOperationException e) {
                    e.printStackTrace();
                }
            }
        } catch (NoSuchPortException e) {
            e.printStackTrace();
        }
    }

    //close
    public void closePort() {
        if (serialPort != null) {
            serialPort.close();
            serialPort = null;
        }
    }

    //send
    public void sendToPort(byte[] order) {
        if (serialPort == null) {
            return;
        }
        OutputStream out = null;

        try {

            out = serialPort.getOutputStream();
            out.write(order);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                    out = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //read
    public byte[] readFromPort() {
        InputStream in = null;
        byte[] bytes = null;
        if (serialPort == null) {
            return bytes;
        }

        try {

            in = serialPort.getInputStream();
            int bufflenth = in.available();

            while (bufflenth != 0) {
                bytes = new byte[bufflenth];
                in.read(bytes);
                bufflenth = in.available();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                    in = null;
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
            serialPort.notifyOnBreakInterrupt(true);

        } catch (TooManyListenersException e) {
            e.printStackTrace();
        }
    }

    private class SerialListener implements SerialPortEventListener {

        private DataAvailableListener availableListener;

        public SerialListener(DataAvailableListener availableListener) {
            this.availableListener = availableListener;
        }

        public void serialEvent(SerialPortEvent serialPortEvent) {

            switch (serialPortEvent.getEventType()) {

                case SerialPortEvent.BI:

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
