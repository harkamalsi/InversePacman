package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class TableComponent implements Component {
    private Skin skin;
    private Table table;
    private Stage stage;
    public boolean draw;

    public TableComponent() {
        stage = new Stage(new ScreenViewport());
        Container<Table> tableContainer = new Container<Table>();
        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();
        float cw = sw * 0.7f;
        float ch = sh * 0.5f;
        this.draw = true;


        tableContainer.setSize(cw, ch);
        tableContainer.setPosition((sw - cw) / 2.0f, (sh - ch) / 2.0f);
        tableContainer.fillX();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        table = new Table(skin);
        table.debugAll();
        tableContainer.setActor(table);
        stage.addActor(tableContainer);
        Gdx.input.setInputProcessor(stage);
    }

    public void addRow(String nameLabel, String nameText) {
        System.out.println("******************* TABLE COMPONENT CALLED");
        Label nameLabel1 = new Label(nameLabel, skin);
        TextField nameText1 = new TextField(nameText, skin);
        table.add(nameLabel1).expandX();
        table.add(nameText1).width(100);
    }

    public Cell getRow(){
        return table.row();
    }

    public void draw() {
        stage.act();
        stage.draw();
    }

}

