package com.wikigreen.controller.json.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.wikigreen.dto.AccountDTO;

import java.io.Reader;

public class AccountReaderImpl implements AccountReader, AutoCloseable{
    private JsonReader jsonReader;
    private final Gson gson;

    public AccountReaderImpl(Reader reader) {
        this.jsonReader = new JsonReader(reader);
        this.gson = new GsonBuilder().create();
    }

    @Override
    public AccountDTO read() {
        return gson.fromJson(jsonReader, AccountDTO.class);
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
