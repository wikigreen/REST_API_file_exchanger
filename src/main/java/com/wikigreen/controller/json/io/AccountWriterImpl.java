package com.wikigreen.controller.json.io;

import com.google.gson.*;
import com.wikigreen.dto.AccountDTO;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class AccountWriterImpl implements AccountWriter {
    private Writer writer;
    private final Gson gson;

    public AccountWriterImpl(Writer writer) {
        this.writer = writer;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    @Override
    public void write(AccountDTO accountDTO) throws IOException {
        prepareAccountToSerialization(accountDTO);

        String json = gson.toJson(accountDTO);

        writer.write(json);
        writer.flush();
    }

    @Override
    public void writeList(List<AccountDTO> list) throws IOException {
        list.forEach(this::prepareAccountToSerialization);

        String json = gson.toJson(list);

        writer.write(json);
        writer.flush();
    }

    private void prepareAccountToSerialization(AccountDTO accountDTO) {
        if(accountDTO != null && accountDTO.getUser() != null){
            if(accountDTO.getUser().getFiles() != null) {
                accountDTO
                        .getUser()
                        .getFiles()
                        .forEach(fileDTO -> fileDTO
                                .setOwner(null));
            }
            if(accountDTO.getUser().getEvents() != null) {
                accountDTO.getUser().getEvents().forEach(eventDTO -> {
                    if (eventDTO.getFile() != null){
                        eventDTO.getFile().setOwner(null);
                    }
                });
            }
            accountDTO.getUser().setAccount(null);
        }
    }

    @Override
    public void writeEmpty() throws IOException {
        writer.write("{}");
        writer.flush();
    }

    @Override
    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    public Writer getWriter() {
        return writer;
    }

    @Override
    public void close() throws Exception {
        writer.flush();
        writer.close();
    }
}
