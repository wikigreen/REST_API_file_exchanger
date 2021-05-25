package com.wikigreen.controller.json.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.wikigreen.dto.EventDTO;
import com.wikigreen.model.Event;

import java.io.BufferedReader;
import java.io.Reader;

public class EventReaderImpl implements EventReader {
    private JsonReader jsonReader;
    private final Gson gson;

    public EventReaderImpl(BufferedReader reader) {
        this.jsonReader = new JsonReader(reader);
        this.gson = new GsonBuilder().create();
    }

    @Override
    public EventDTO read() {
        return gson.fromJson(jsonReader, EventDTO.class);
    }

    @Override
    public void setReader(Reader reader) {
        this.jsonReader = new JsonReader(reader);
    }

    @Override
    public void close() throws Exception {
        jsonReader.close();
    }
}
