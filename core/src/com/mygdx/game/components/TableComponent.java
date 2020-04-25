package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import javax.annotation.processing.SupportedSourceVersion;
import javax.swing.event.ChangeEvent;

public class TableComponent implements Component {
    private Skin skin;
    private Table table;
    private Stage stage;
    public boolean draw;
    public boolean getLobbies;
    public String joinLobbyName = "";
    public boolean lobbyButtonClicked = false;
    public boolean createLobby = false;
    public boolean isReadyUpChecked = false;
    public static String PLAYERTYPE = "GHOST";
    public boolean pacmanType = false;
    public boolean ghostType = true;
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
        /*TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = BitmapFont();
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.downFontColor = Color.RED;*/
        final TextButton lobbyButton = new TextButton(nameLabel, skin); //skin
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
                lobbyButtonClicked = true;
                //System.out.println("CLIECKEDDDDDDDDDD");
                //lobbyButton.setStyle(skin.get("chosen", TextButton.TextButtonStyle.class));
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

    public void addCheckbox() {
        final CheckBox readyUpCheckbox = new CheckBox(" Ready?", skin);

        readyUpCheckbox.getLabel().setFontScale(0.5f);
        readyUpCheckbox.setSize(50f, 50f);
        readyUpCheckbox.getImage().setScaling(Scaling.fit);
        readyUpCheckbox.getImageCell().size(50f, 50f);
        readyUpCheckbox.setChecked(isReadyUpChecked);

        readyUpCheckbox.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isReadyUpChecked = !isReadyUpChecked;
                readyUpCheckbox.setChecked(isReadyUpChecked);

                return true;
            }
        });

        table.add(readyUpCheckbox).expandX().padBottom(20);
    }

    public void addTypeCheckboxes() {
        final CheckBox pacmantypeCheckBox = new CheckBox("PACMAN", skin);
        final CheckBox ghostTypeCheckBox = new CheckBox("GHOST", skin);

        pacmantypeCheckBox.getLabel().setFontScale(0.5f);
        pacmantypeCheckBox.setSize(50f, 50f);
        pacmantypeCheckBox.getImage().setScaling(Scaling.fit);
        pacmantypeCheckBox.getImageCell().size(50f, 50f);
        pacmantypeCheckBox.setChecked(pacmanType);

        ghostTypeCheckBox.getLabel().setFontScale(0.5f);
        ghostTypeCheckBox.setSize(50f, 50f);
        ghostTypeCheckBox.getImage().setScaling(Scaling.fit);
        ghostTypeCheckBox.getImageCell().size(50f, 50f);
        ghostTypeCheckBox.setChecked(ghostType);

        pacmantypeCheckBox.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (ghostType) {
                    return true;
                }

                pacmanType = !pacmanType;
                ghostTypeCheckBox.setChecked(pacmanType);

                return true;
            }
        });

        ghostTypeCheckBox.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pacmanType) {
                    return true;
                }

                ghostType = !ghostType;
                ghostTypeCheckBox.setChecked(ghostType);

                return true;
            }
        });

        /*final SelectBox<String> selectBox = new SelectBox<String>(skin);
        Array<String> options = new Array<String>();
        options.add("GHOST");
        options.add("PACMAN");
        selectBox.setItems(options);
        selectBox.setSelected(playerType);

        selectBox.addCaptureListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                playerType = selectBox.getSelected();
                selectBox.setSelected(playerType);
                return false;
            }
        });*/

        if (ghostType) {
            PLAYERTYPE = "GHOST";
        } else {
            PLAYERTYPE = "PACMAN";
        }

        table.row();
        table.add(ghostTypeCheckBox).expandX();
        table.add(pacmantypeCheckBox).expandX();
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

