package com.wikigreen.controller.json.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.wikigreen.dto.UserDTO;

import java.io.Reader;

public class UserReaderImpl implements UserReader {
    private JsonReader jsonReader;
    private final Gson gson;

    public UserReaderImpl (Reader reader) {
        this.jsonReader = new JsonReader(reader);
        this.gson = new GsonBuilder().create();
    }

    @Override
    public UserDTO read() {
        return gson.fromJson(jsonReader, UserDTO.class);
    }

    @Override
    public void setReader(Reader reader) {
        this.jsonReader = gson.newJsonReader(reader);
    }

    @Override
    public void close() throws Exception {
        jsonReader.close();
    }
}
