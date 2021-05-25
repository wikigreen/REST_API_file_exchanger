package com.wikigreen.controller.json.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wikigreen.dto.DtoUtils;
import com.wikigreen.dto.UserDTO;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class UserWriterImpl implements UserWriter {
    private Writer writer;
    private final Gson gson;
    private static final DtoUtils DTO_UTILS = new DtoUtils();

    public UserWriterImpl(Writer writer) {
        this.writer = writer;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    @Override
    public void write(UserDTO userDTO) throws IOException {
        prepareUserToSerialization(userDTO);

        String json = gson.toJson(userDTO);

        writer.write(json);
        writer.flush();
    }

    @Override
    public void writeList(List<UserDTO> list) throws IOException {
        list.forEach(this::prepareUserToSerialization);

        String json = gson.toJson(list);

        writer.write(json);
        writer.flush();
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

    @Override
    public void close() throws Exception {
        writer.flush();
        writer.close();
    }

    private void prepareUserToSerialization(UserDTO userDTO) {
        if(userDTO != null){
            FileWriterImpl.prepareUser(userDTO);
        }
    }
}
