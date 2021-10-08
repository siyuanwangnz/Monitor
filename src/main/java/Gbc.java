package main.java;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

public class Gbc extends GridBagConstraints {
    public static final Insets INSET2 = new Insets(2, 2, 2, 2);
    public static final Insets INSET5 = new Insets(5, 5, 5, 5);
    private Map<Integer, Gbc> gbcForColumn;

    public Gbc(int aStartX, int aStartY) {
        this.gbcForColumn = new HashMap();
        this.gridy = aStartX;
        this.gridx = aStartY;
    }

    public Gbc() {
        this(0, 0);
    }

    public Gbc clone() {
        return (Gbc)super.clone();
    }

    public Gbc forColumn(Integer colIndex) {
        assert colIndex > 0;

        Gbc gbc = (Gbc)this.gbcForColumn.get(colIndex);
        if (gbc == null) {
            gbc = this.clone();
            this.gbcForColumn.put(colIndex, gbc);
        }

        return gbc;
    }

    public Gbc nextX(int aWidth) {
        this.gridx += this.gridwidth;
        this.width(aWidth);
        return this.selectGbc(this.gridx);
    }

    public Gbc nextX() {
        this.gridx += this.gridwidth;
        this.gridwidth = 1;
        return this.selectGbc(this.gridx);
    }

    public Gbc first() {
        this.gridx = 0;
        this.gridy = 0;
        return this.selectGbc(this.gridx);
    }

    public Gbc north() {
        --this.gridy;
        return this.selectGbc(this.gridx);
    }

    public Gbc south() {
        ++this.gridy;
        return this.selectGbc(this.gridx);
    }

    public Gbc east() {
        return this.nextX();
    }

    public Gbc west() {
        --this.gridx;
        return this.selectGbc(this.gridx);
    }

    public Gbc width(int cols) {
        this.gridwidth = cols;
        return this;
    }

    public Gbc wholeRow() {
        this.gridx = 0;
        this.gridwidth = 200;
        ++this.gridy;
        return this;
    }

    public Gbc nextRow() {
        this.gridx = 0;
        this.gridwidth = 1;
        ++this.gridy;
        return this.selectGbc(this.gridx);
    }

    private Gbc selectGbc(Integer aGridx) {
        Integer colIndex = aGridx + 1;
        if (this.gbcForColumn.containsKey(colIndex)) {
            Gbc gbc = (Gbc)this.gbcForColumn.get(colIndex);
            gbc.gridx = this.gridx;
            gbc.gridy = this.gridy;
            gbc.gridwidth = this.gridwidth;
            return gbc;
        } else {
            return this;
        }
    }

    public Memento memento() {
        return new Memento() {
            private final Gbc gbc = Gbc.this.clone();

            public void rollback() {
                Gbc.this.weightx = this.gbc.weightx;
                Gbc.this.weighty = this.gbc.weighty;
                Gbc.this.anchor = this.gbc.anchor;
                Gbc.this.fill = this.gbc.fill;
                Gbc.this.insets = this.gbc.insets;
                Gbc.this.ipadx = this.gbc.ipadx;
                Gbc.this.ipady = this.gbc.ipady;
            }
        };
    }
}
