package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class TableComponent implements Component {
    private Table table;

    public TableComponent() {
        table = new Table();
    }

    public void addRow(String nameLabel, String nameText) {
        System.out.println("******************* TABLE COMPONENT CALLED");

        table.add(nameLabel).expandX();
        table.add(nameText).width(100);

        table.row();
    }

}

