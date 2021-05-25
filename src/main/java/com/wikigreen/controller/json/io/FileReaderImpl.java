package com.wikigreen.controller.json.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.wikigreen.dto.FileDTO;

import java.io.Reader;

public class FileReaderImpl implements FileReader{
    private JsonReader jsonReader;
    private final Gson gson;

    public FileReaderImpl(Reader reader) {
        this.jsonReader = new JsonReader(reader);
        this.gson = new GsonBuilder().create();
    }

    @Override
    public FileDTO read() {
        return gson.fromJson(jsonReader, FileDTO.class);
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
