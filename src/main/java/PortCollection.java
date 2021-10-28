package main.java;

import javax.swing.*;
import java.util.*;
import java.util.Timer;

public class PortCollection {
    private Port port;
    private DefaultListModel portList = new DefaultListModel();

    public PortCollection() {
        port = Port.getInstance();
        java.util.Timer timer = new Timer();
        timer.scheduleAtFixedRate(new PingerTask(), 500, 5000);

    }

    public DefaultListModel getPortList() {
        return portList;
    }

    public Port getPort() {
        return port;
    }

    private class PingerTask extends TimerTask {

        @Override
        public void run() {
            synchronized (Port.class) {
                DefaultListModel newList = new DefaultListModel();
                Set<String> list = port.findPort();
                if (list != null || list.size() > 0) {
                    for (String com : list) {
                        newList.addElement(com);
                    }
                }
                //add new com
                Enumeration en = newList.elements();
                while (en.hasMoreElements()) {
                    Object obj = en.nextElement();

                    if (!portList.contains(obj)) portList.addElement(obj);
                }
                //delete unavailable com
                en = portList.elements();
                while (en.hasMoreElements()) {
                    Object obj = en.nextElement();

                    // Cope with port no longer visible
                    //
                    if (!newList.contains(obj)) {
                        portList.removeElement(obj);
                    }
                }

            }
        }
    }

}
