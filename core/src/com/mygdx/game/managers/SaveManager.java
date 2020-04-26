package com.mygdx.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Using example from LibGDX Game Development Essentials.
 * This can be used for saving a singe player (i.e., local) scoreboard.
 */
public class SaveManager {
    private boolean encoded;
    private Save save;

    public SaveManager(boolean encoded) {
        this.encoded = encoded;
        save = getSave();
    }

    public static class Save {
        public ObjectMap<String, Object> data = new ObjectMap<String, Object>();
    }

    private FileHandle file = Gdx.files.local("bin/ids.json");

    private Save getSave() {
        Save save = new Save();

        if (file.exists()) {
            Json json = new Json();
            if (encoded) {
                save = json.fromJson(Save.class, Base64Coder.decodeString(file.readString()));
            } else {
                save = json.fromJson(Save.class, file.readString());
            }
        }

        return save;
    }

    private void saveToJson() {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        if (encoded) {
            file.writeString(Base64Coder.encodeString(json.prettyPrint(save)), false);
        } else {
            file.writeString(json.prettyPrint(save), false);
        }
    }

    public <T> T loadDataValue(String key, Class<? extends T> type) {
        T dataValue = null;
        if (save.data.containsKey(key)) {
            dataValue = type.cast(save.data.get(key));
        }

        return dataValue;
    }

    public void saveDataValue(String key, Object object) {
        save.data.put(key, object);
        saveToJson();
    }

    public ObjectMap<String, Object> getAllData() {
        return save.data;
    }

}
