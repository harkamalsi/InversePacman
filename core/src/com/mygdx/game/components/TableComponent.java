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
    public boolean getLobbies;
    private Container<Table> tableContainer;

    public TableComponent() {
        stage = new Stage(new ScreenViewport());
        tableContainer = new Container<Table>();
        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();
        float cw = sw * 1f;
        float ch = sh * 0.5f;
        this.draw = true;
        this.getLobbies = true;


        tableContainer.setSize(cw, ch);
        tableContainer.setPosition((sw - cw) / 2.0f, (sh - ch) / 2.0f);
        tableContainer.fillX();

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        skin.getFont("default-font").getData().setScale(5f, 5f);
        table = new Table(skin);
        tableContainer.setActor(table);
        stage.addActor(tableContainer);
        Gdx.input.setInputProcessor(stage);
    }

    public void addRow(String nameLabel, String nameText) {
        Label lobbyName = new Label(nameLabel, skin);
        Label lobbyPlayers = new Label(nameText, skin);

        table.add(lobbyName).expand();
        table.add(lobbyPlayers).expand();
        table.row();
    }

    public Cell getRow(){
        return table.row();
    }

    public void draw() {
        System.out.println(stage.getActors());
        stage.act();
        stage.draw();
    }

    public void reset() {
        draw = true;
        table.clearChildren();
    }

}

