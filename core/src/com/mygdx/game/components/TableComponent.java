package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import javax.swing.event.ChangeEvent;

public class TableComponent implements Component {
    private Skin skin;
    private Table table;
    private Stage stage;
    public boolean draw;
    public boolean getLobbies;
    public String joinLobbyName = "";
    public boolean joinLobby = false;
    public boolean createLobby = false;
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

        skin = new Skin(Gdx.files.internal("font/uiskin.json"));
        skin.getFont("default-font").getData().setScale(1f, 1f);
        table = new Table(skin);
        tableContainer.setActor(table);
        stage.addActor(tableContainer);
        Gdx.input.setInputProcessor(stage);
    }

    public void addRow(final String nameLabel, String nameText) {
        TextButton lobbyButton = new TextButton(nameLabel, skin);
        Label lobbyPlayers = new Label(nameText, skin);

        table.add(lobbyButton).expand().padBottom(20);
        table.add(lobbyPlayers).expand().padBottom(20);

        lobbyButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                joinLobbyName = nameLabel;
                joinLobby = true;
                return true;
            }
        });

        table.row();
    }

    public void addConnectingToServerMessage() {
        Label message1 = new Label("Connecting to", skin);
        Label message2 = new Label(" server...", skin);
        message1.setFontScale(0.9f);
        message2.setFontScale(0.9f);
        table.add(message1).padBottom(20);
        table.row();
        table.add(message2);
        table.row();
    }

    public String getJoinLobbyName() {
        return joinLobbyName;
    }

    public void draw() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void reset() {
        draw = true;
        table.clearChildren();
    }


}

