import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;

public class SerialView extends JPanel {
    private final JPanel north;
    private final JPanel west;
    private final JPanel centre;
    private final String name;
    private final PortCollection portCollection;

    public SerialView(String title) {
        portCollection = new PortCollection();

        name = "Serial " + title;
        setName(name);

        setLayout(new BorderLayout());

        north = buildNorth();
        centre = buildCentre();

        west = new JPanel();
        west.setBorder(BorderFactory.createTitledBorder("Port"));
        west.add(new JComboBox(new String[]{"NULL"}));

        add(north, BorderLayout.NORTH);
        add(west, BorderLayout.WEST);
        add(centre, BorderLayout.CENTER);

        portCollection.getPortList().addListDataListener(new ListDataListener() {

            @Override
            public void intervalAdded(ListDataEvent e) {
                refreshComDisplay();
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                refreshComDisplay();
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                refreshComDisplay();
            }
        });
    }

    private void refreshComDisplay() {
        west.removeAll();
        buildWest();
        add(north, BorderLayout.NORTH);
        add(west, BorderLayout.WEST);
        add(centre, BorderLayout.CENTER);
    }

    private JPanel buildNorth() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Received Message"));

        JTextArea textArea = new JTextArea(21, 51);
        textArea.setLineWrap(true);
        textArea.setEditable(false);

        panel.add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        return panel;
    }

    private void buildWest() {
        JComboBox comboBox = new JComboBox();

        DefaultListModel list = portCollection.getPortList();
        if (list != null || list.size() > 0) {
            for (int i = 0; i < list.getSize(); i++) {
                Object element = list.getElementAt(i);
                comboBox.addItem(element.toString());
            }
        }
        comboBox.addItem("NULL");

        west.add(comboBox);
    }

    private JPanel buildCentre() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Send Message"));

        JTextField textField = new JFormattedTextField();
        textField.setColumns(30);

        JButton btnSend = new JButton("Send");
        JButton btnClear = new JButton("Clear");

        panel.add(textField);
        panel.add(btnSend);
        panel.add(btnClear);

        return panel;
    }
}
