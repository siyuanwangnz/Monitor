package main.java;

import javax.swing.*;
import java.awt.*;

public class TabView {
    private DefaultListModel<JPanel> viewList = new DefaultListModel<>();
    private JTabbedPane tab = new JTabbedPane();

    public TabView() {
    }

    public TabView(DefaultListModel<JPanel> viewList) {
        this.viewList = viewList;
    }

    public Integer getNextViewNum() {
        return viewList.size() + 1;
    }

    public void addView(JPanel panel) {
        if (viewList.size() > 5) {
            return;
        }
        viewList.addElement(panel);
    }

    public void removeView() {
        if (viewList.size() < 2) {
            return;
        }
        viewList.removeElementAt(viewList.size() - 1);
    }

    public void tabConfig(Container main) {
        if (main == null) {
            return;
        }
        JTabbedPane tab = new JTabbedPane();
        for (int i = 0; i < viewList.getSize(); i++) {
            JPanel view = (JPanel) viewList.getElementAt(i);
            tab.addTab(view.getName(), view);
        }
        main.add(tab, BorderLayout.CENTER);
    }

    public void tabConfig(Container main, int x, int y, int width, int height) {
        if (main == null) {
            return;
        }
        main.remove(tab);
        tab = new JTabbedPane();
        for (int i = 0; i < viewList.getSize(); i++) {
            JPanel view = (JPanel) viewList.getElementAt(i);
            tab.addTab(view.getName(), view);
        }
        tab.setBounds(x, y, width, height);
        main.add(tab);
    }
}
