package com.wikigreen.controller.json.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wikigreen.dto.FileDTO;
import com.wikigreen.dto.UserDTO;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class FileWriterImpl implements FileWriter {
    private Writer writer;
    private final Gson gson;

    public FileWriterImpl(Writer writer) {
        this.writer = writer;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    @Override
    public void write(FileDTO fileDTO) throws IOException {
        prepareFileToSerialization(fileDTO);

        String json = gson.toJson(fileDTO);

        writer.write(json);
        writer.flush();
    }

    @Override
    public void writeList(List<FileDTO> list) throws IOException {
        list.forEach(this::prepareFileToSerialization);

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

    private void prepareFileToSerialization(FileDTO fileDTO) {
        if (fileDTO != null && fileDTO.getOwner() != null) {
            UserDTO owner = fileDTO.getOwner();

            if (owner.getAccountDTO() != null) {
                prepareUser(owner);
            }
        }
    }

    static void prepareUser(UserDTO owner) {
        if (owner.getFiles() != null) {
            owner.getFiles().forEach(fileDto -> fileDto.setOwner(null));
        }

        if (owner.getEvents() != null) {
            owner.getEvents().forEach(eventDTO -> {
                if (eventDTO.getFile() != null){
                    eventDTO.getFile().setOwner(null);
                }
            });
        }

        if (owner.getAccountDTO() != null){
            owner.getAccountDTO().setUserDTO(null);
        }
    }
}
