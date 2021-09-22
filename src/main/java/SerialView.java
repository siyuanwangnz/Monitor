import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SerialView extends JPanel {
    private final JPanel north;
    private final JPanel west;
    private final JPanel centre;
    private final String name;
    private final Port port;

    public SerialView(String title) {
        port = Port.getInstance();

        name = "Serial " + title;
        setName(name);

        setLayout(new BorderLayout());
        setOpaque(false);

        north = buildNorth();
        west = buildWest();
        centre = buildCentre();

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

    private JPanel buildWest() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Port"));

        JComboBox comboBox = new JComboBox();
        ArrayList<String> portList = port.findPort();
        if (portList != null || portList.size() > 0)
        {
            for (String com : port.findPort()) {
                comboBox.addItem(com);
            }
        }
        comboBox.addItem("NULL");

        panel.add(comboBox);

        return panel;
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
