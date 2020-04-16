package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class TableComponent implements Component {
    private Skin skin;
    private Table table;

    public TableComponent() {
        skin = new Skin();
        table = new Table(skin);

    }

    public void addRow(String nameLabel, String nameText) {
        System.out.println("******************* TABLE COMPONENT CALLED");
        Label nameLabel1 = new Label(nameLabel, skin);
        TextField nameText1 = new TextField(nameText, skin);
        table.add(nameLabel1).expandX();
        table.add(nameText1).width(100);

        table.row();
    }

}

