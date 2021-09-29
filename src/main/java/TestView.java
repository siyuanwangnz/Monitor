import javax.swing.*;
import java.awt.*;

public class TestView extends JPanel {
    private final String name;

    public TestView(String name) {
        this.name = name;
        setName(this.name);
        setBorder(BorderFactory.createTitledBorder("Pressure Controller"));

        add(panelCreation());
    }

    private JPanel panelCreation() {
        JPanel panelMain = new JPanel(new GridBagLayout());
        Gbc gbc = new Gbc();
        gbc.insets = Gbc.INSET5;

        panelMain.add(LineChreation("Prop1"),gbc.nextRow());
        panelMain.add(LineChreation("Prop2"),gbc.nextRow());


        return panelMain;
    }

    private JPanel LineChreation(String name) {
        JPanel panel = new JPanel(new FlowLayout());

        panel.add(new JLabel(name + ": "));

        ButtonGroup buttonGroup = new ButtonGroup();
        JRadioButton btn1 = new JRadioButton("off");
        JRadioButton btn2 = new JRadioButton("Man.");

        buttonGroup.add(btn1);
        buttonGroup.add(btn2);
        panel.add(btn1);
        panel.add(btn2);

        panel.add(new JLabel("Position"));

        JSpinner spinner1 = new JSpinner();
        spinner1.setValue(2000);
        panel.add(spinner1);

        panel.add(new JLabel("%"));

        panel.add(new JLabel("Step"));

        JSpinner spinner2 = new JSpinner();
        spinner2.setValue(1000);
        panel.add(spinner2);

        panel.add(new JLabel("%"));

        return panel;
    }
}
