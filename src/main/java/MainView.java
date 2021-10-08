package main.java;

import javafx.scene.control.Tab;

import javax.swing.*;
import java.awt.*;

public class MainView {
    private JFrame main;
    private String title;
    private TabView tabView;

    public MainView(String title) {
        this.title = title;

        main = new JFrame();

        DefaultListModel<JPanel> viewList = new DefaultListModel<>();

        viewList.addElement(new SerialView("1"));
        viewList.addElement(new TestView("Test"));

        tabView = new TabView(viewList);
        tabView.tabConfig(main);

        mainConfig();
    }

    private void mainConfig() {
        main.setTitle(title);
        main.setBounds(500, 300, 600, 500);
        main.setResizable(false);
        main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        main.setVisible(true);
    }
}
