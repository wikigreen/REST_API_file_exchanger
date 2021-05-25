package com.wikigreen.controller.json.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wikigreen.dto.EventDTO;
import com.wikigreen.dto.UserDTO;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

public class EventWriterImpl implements EventWriter {
    private Writer writer;
    private final Gson gson;

    public EventWriterImpl(PrintWriter writer) {
        this.writer = writer;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    @Override
    public void write(EventDTO eventDTO) throws IOException {
        prepareEventToSerialization(eventDTO);

        String json = gson.toJson(eventDTO);

        writer.write(json);
        writer.flush();
    }

    private void prepareEventToSerialization(EventDTO eventDTO) {
        if (eventDTO != null
                && eventDTO.getFile() != null
                && eventDTO.getFile().getOwner() != null) {
            UserDTO userDTO = eventDTO.getFile().getOwner();

            if (userDTO.getAccountDTO() != null) {
                FileWriterImpl.prepareUser(userDTO);
            }
        }
    }

    @Override
    public void writeList(List<EventDTO> list) throws IOException {
        list.forEach(this::prepareEventToSerialization);

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
}
