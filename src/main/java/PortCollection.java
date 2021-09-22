import javax.swing.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PortCollection {
    private Port port;
    private DefaultListModel portList= new DefaultListModel();

    public PortCollection() {
        port = Port.getInstance();
        java.util.Timer timer = new Timer();
        timer.scheduleAtFixedRate(new PingerTask(), 5000, 10000);

    }

    public DefaultListModel getPortList(){
        return portList;
    }

    private class PingerTask extends TimerTask {

        @Override
        public void run() {
            synchronized (Port.class) {
                portList.removeAllElements();

                ArrayList<String> list = port.findPort();
                if (list != null || list.size() > 0) {
                    for (String com : list) {
                        portList.addElement(com);
                    }
                }
            }
        }
    }

}
