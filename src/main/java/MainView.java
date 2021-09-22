import javax.swing.*;
import java.awt.*;

public class MainView {
    public static void main(String[] args) {
        new MainView();
    }

    private JFrame main;

    public MainView() {
        main = new JFrame();

        DefaultListModel<JPanel> viewList = new DefaultListModel<>();

        viewList.addElement(new SerialView("1"));
        viewList.addElement(new SerialView("2"));

        tabConfig(viewList);
        mainConfig();
    }

    private void mainConfig() {
        main.setTitle("Monitor");
        main.setBounds(500, 300, 600, 500);
        main.setResizable(false);
        main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        main.setVisible(true);
    }

    private void tabConfig(DefaultListModel<JPanel> viewList) {
        JTabbedPane tab = new JTabbedPane();
        for (int i = 0; i < viewList.getSize(); i++) {
            JPanel view = (JPanel) viewList.getElementAt(i);
            tab.addTab(view.getName(), view);
        }
        main.add(tab, BorderLayout.CENTER);
    }
}
