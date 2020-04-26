package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.managers.SaveManager;

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
    private SaveManager saveManager;

    public TableComponent(SaveManager saveManager) {
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

        this.saveManager = saveManager;
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

    public void addPlayerNickname() {
        String nickname = saveManager.loadDataValue("nickname", String.class);
        if (nickname != null || !nickname.isEmpty()) {
            Label nickNameMessage = new Label("Nickname: ", skin);
            Label nickName = new Label(nickname, skin);
            nickNameMessage.setFontScale(0.5f);
            nickName.setFontScale(0.5f);
            table.add(nickNameMessage).expandX().padBottom(150);
            table.add(nickName).expandX().padBottom(150);
            table.row();
        }
    }

    public void addRow(final String nameLabel, String nameText) {
        final TextButton lobbyButton = new TextButton(nameLabel, skin); //skin
        Label lobbyPlayers = new Label(nameText, skin);

        table.add(lobbyButton).expandX().padBottom(30);
        table.add(lobbyPlayers).expandX().padBottom(30);

        lobbyButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                lobbyButtonClicked = true;
                joinLobbyName = nameLabel;
                return true;
            }
        });

        table.row();
    }

    public void addReadyUpCheckbox() {
        final CheckBox readyUpCheckbox = new CheckBox(" Ready?", skin);

        readyUpCheckbox.getLabel().setFontScale(0.7f);
        readyUpCheckbox.getImage().setScaling(Scaling.fit);
        readyUpCheckbox.getImageCell().size(60f, 60f);
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

        table.add(readyUpCheckbox).padTop(150).padBottom(100).colspan(2);
        table.row();
    }

    public void addTypeCheckboxes() {
        final CheckBox pacmantypeCheckBox = new CheckBox(" PACMAN", skin);
        final CheckBox ghostTypeCheckBox = new CheckBox(" GHOST", skin);

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

        if (ghostType) {
            PLAYERTYPE = "GHOST";
        } else {
            PLAYERTYPE = "PACMAN";
        }

        table.add(ghostTypeCheckBox).expandX();
        table.add(pacmantypeCheckBox).expandX();
        table.row();
    }

    public String getJoinLobbyName() {
        return joinLobbyName;
    }

    public void resetJoinLobbyName() {
        joinLobbyName = "";
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

