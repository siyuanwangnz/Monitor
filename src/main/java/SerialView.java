import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class SerialView extends JPanel {
    private final JPanel north;
    private final JPanel west;
    private final JPanel centre;
    private final String name;
    private final PortCollection portCollection;
    private Listenable<String> listenable = new Listenable<>();

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
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Received Message"));

        final JTextArea textArea = new JTextArea(20, 51);
        textArea.setLineWrap(true);
        textArea.setEditable(false);

        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
            }
        });

        listenable.addListener(new Consumer<String>() {
            @Override
            public void accept(String s) {
                textArea.append(s);
            }
        });

        panel.add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.NORTH);
        panel.add(btnClear, BorderLayout.SOUTH);

        return panel;
    }

    private void buildWest() {
        JComboBox comboBox = new JComboBox();
        comboBox.addItem("NULL");

        final DefaultListModel list = portCollection.getPortList();
        if (list != null || list.size() > 0) {
            for (int i = 0; i < list.getSize(); i++) {
                Object element = list.getElementAt(i);
                comboBox.addItem(element.toString());
            }
        }

        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (e.getItem().toString() == "NULL") {
                        //close port
                        portCollection.getPort().closePort();
                        System.out.println(portCollection.getPort().getPortName());
                    } else {
                        //open port
                        portCollection.getPort().openPort(e.getItem().toString(), 115200);
                        System.out.println(portCollection.getPort().getPortName());
                        //add listener
                        portCollection.getPort().addListener(new Port.DataAvailableListener() {
                            @Override
                            public void dataAvailable() {
                                byte[] data = portCollection.getPort().readFromPort();
                                listenable.setValue(new String(data));
                                System.out.println(new String(data));
                            }
                        });
                    }
                }
            }
        });

        west.add(comboBox);
    }

    private JPanel buildCentre() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Send Message"));

        final JTextField textField = new JFormattedTextField();
        textField.setColumns(25);

        JCheckBox checkBox = new JCheckBox("+ CR LF");
        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox cb = (JCheckBox) e.getSource();
                if (cb.isSelected()) {
                    portCollection.setEnableEOL(true);
                    System.out.println("+EOL Enable");
                } else {
                    portCollection.setEnableEOL(false);
                    System.out.println("+EOL Disable");
                }
            }
        });

        JButton btnSend = new JButton("Send");
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String data = textField.getText().toString() + portCollection.getEOL();
                portCollection.getPort().sendToPort(data.getBytes());
                System.out.println(data);
            }
        });

        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField.setText("");
            }
        });

        panel.add(textField);
        panel.add(checkBox);
        panel.add(btnSend);
        panel.add(btnClear);


        return panel;
    }

    private class Listenable<E> {
        private E value;

        private Consumer<E> consumer;

        public void addListener(Consumer<E> consumer) {
            this.consumer = consumer;
        }

        public void setValue(E value) {
            this.value = value;
            if (consumer != null) {
                consumer.accept(value);
            }
        }
    }

    public interface Consumer<T> {
        void accept(T t);
    }
}
