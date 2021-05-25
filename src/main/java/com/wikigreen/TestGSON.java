package com.wikigreen;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wikigreen.controller.json.io.*;
import com.wikigreen.dao.DaoException;
import com.wikigreen.dto.AccountDTO;
import com.wikigreen.dto.EventDTO;
import com.wikigreen.dto.FileDTO;
import com.wikigreen.dto.UserDTO;
import com.wikigreen.model.*;
import com.wikigreen.model.File;
import com.wikigreen.service.FileService;
import com.wikigreen.service.implementations.FileServiceImpl;

import java.io.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class TestGSON {

    public static void main(String[] args) throws IOException, DaoException {
        FileService fileService = new FileServiceImpl();

        File file = fileService.getById(65L);
    }

    public static void writeFullFile() throws IOException {
        com.wikigreen.controller.json.io.FileWriter fileWriter = new FileWriterImpl(new FileWriter("out.json"));

        UserDTO userDTO = new UserDTO(
                1L,
                new AccountDTO(1L, null, "userTest@gmail.com", "userTest", AccountStatus.ACTIVE),
                new ArrayList<>(),
                new ArrayList<>(),
                "User", "Test"
        );

        userDTO.getAccountDTO().setUserDTO(userDTO);
        userDTO.setFiles(List.of(
                new FileDTO(1L, "userTest.txt", 1024000L, userDTO),
                new FileDTO(1L, "userTest.txt", 1024000L, userDTO),
                new FileDTO(1L, "userTest.txt", 1024000L, userDTO),
                new FileDTO(1L, "userTest.txt", 1024000L, userDTO)
        ));

        userDTO.setEvents(List.of(
                new EventDTO(1L, EventType.CREATION, userDTO.getFiles().get(0)),
                new EventDTO(1L, EventType.DOWNLOADING, userDTO.getFiles().get(0)),
                new EventDTO(1L, EventType.UPDATE, userDTO.getFiles().get(0)),
                new EventDTO(1L, EventType.CREATION, userDTO.getFiles().get(1)),
                new EventDTO(1L, EventType.DOWNLOADING, userDTO.getFiles().get(1)),
                new EventDTO(1L, EventType.UPDATE, userDTO.getFiles().get(1)),
                new EventDTO(1L, EventType.CREATION, userDTO.getFiles().get(2)),
                new EventDTO(1L, EventType.DOWNLOADING, userDTO.getFiles().get(2)),
                new EventDTO(1L, EventType.UPDATE, userDTO.getFiles().get(2)),
                new EventDTO(1L, EventType.CREATION, userDTO.getFiles().get(3)),
                new EventDTO(1L, EventType.DOWNLOADING, userDTO.getFiles().get(3)),
                new EventDTO(1L, EventType.UPDATE, userDTO.getFiles().get(3))
        ));
        userDTO.getAccountDTO().setUserDTO(userDTO);

        fileWriter.write(userDTO.getFiles().get(0));
    }

    public static void writeFullUser() throws IOException {
        UserWriter userWriter = new UserWriterImpl(new FileWriter("out.json"));

        UserDTO userDTO = new UserDTO(
                1L,
                new AccountDTO(1L, null, "userTest@gmail.com", "userTest", AccountStatus.ACTIVE),
                new ArrayList<>(),
                new ArrayList<>(),
                "User", "Test"
        );

        userDTO.getAccountDTO().setUserDTO(userDTO);
        userDTO.setFiles(List.of(
                new FileDTO(1L, "userTest.txt", 1024000L, userDTO),
                new FileDTO(1L, "userTest.txt", 1024000L, userDTO),
                new FileDTO(1L, "userTest.txt", 1024000L, userDTO),
                new FileDTO(1L, "userTest.txt", 1024000L, userDTO)
        ));

        userDTO.setEvents(List.of(
                new EventDTO(1L, EventType.CREATION, userDTO.getFiles().get(0)),
                new EventDTO(1L, EventType.DOWNLOADING, userDTO.getFiles().get(0)),
                new EventDTO(1L, EventType.UPDATE, userDTO.getFiles().get(0)),
                new EventDTO(1L, EventType.CREATION, userDTO.getFiles().get(1)),
                new EventDTO(1L, EventType.DOWNLOADING, userDTO.getFiles().get(1)),
                new EventDTO(1L, EventType.UPDATE, userDTO.getFiles().get(1)),
                new EventDTO(1L, EventType.CREATION, userDTO.getFiles().get(2)),
                new EventDTO(1L, EventType.DOWNLOADING, userDTO.getFiles().get(2)),
                new EventDTO(1L, EventType.UPDATE, userDTO.getFiles().get(2)),
                new EventDTO(1L, EventType.CREATION, userDTO.getFiles().get(3)),
                new EventDTO(1L, EventType.DOWNLOADING, userDTO.getFiles().get(3)),
                new EventDTO(1L, EventType.UPDATE, userDTO.getFiles().get(3))
        ));
        userDTO.getAccountDTO().setUserDTO(userDTO);

        userWriter.write(userDTO);
    }

    public static void writeFullAccount() throws IOException {
        AccountWriter accountWriter = new AccountWriterImpl(new FileWriter("out.json"));

        UserDTO userDTO = new UserDTO(
                1L,
                new AccountDTO(1L, null, "rick_sanchez@gmail.com", "rick", AccountStatus.ACTIVE),
                new ArrayList<>(),
                new ArrayList<>(),
                "Rick", "Sanchez"
        );

        userDTO.getAccountDTO().setUserDTO(userDTO);
        userDTO.setFiles(List.of(
                new FileDTO(1L, "RickFile1.txt", 1024000L, userDTO),
                new FileDTO(1L, "RickFile2.txt", 1024000L, userDTO),
                new FileDTO(1L, "RickFile3.txt", 1024000L, userDTO),
                new FileDTO(1L, "RickFile4.txt", 1024000L, userDTO)
        ));

        userDTO.setEvents(List.of(
                new EventDTO(1L, EventType.CREATION, userDTO.getFiles().get(0)),
                new EventDTO(1L, EventType.DOWNLOADING, userDTO.getFiles().get(0)),
                new EventDTO(1L, EventType.UPDATE, userDTO.getFiles().get(0)),
                new EventDTO(1L, EventType.CREATION, userDTO.getFiles().get(1)),
                new EventDTO(1L, EventType.DOWNLOADING, userDTO.getFiles().get(1)),
                new EventDTO(1L, EventType.UPDATE, userDTO.getFiles().get(1)),
                new EventDTO(1L, EventType.CREATION, userDTO.getFiles().get(2)),
                new EventDTO(1L, EventType.DOWNLOADING, userDTO.getFiles().get(2)),
                new EventDTO(1L, EventType.UPDATE, userDTO.getFiles().get(2)),
                new EventDTO(1L, EventType.CREATION, userDTO.getFiles().get(3)),
                new EventDTO(1L, EventType.DOWNLOADING, userDTO.getFiles().get(3)),
                new EventDTO(1L, EventType.UPDATE, userDTO.getFiles().get(3))
        ));
        userDTO.getAccountDTO().setUserDTO(userDTO);

        accountWriter.write(userDTO.getAccountDTO());
    }

    public static void printJSON() throws IOException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        JsonWriter jsonWriter = new JsonWriter(new FileWriter("out.json"));

        jsonWriter.setIndent("\t");
        gson.toJson(null,
                new TypeToken<Test>(){}.getType(),
                jsonWriter);

        jsonWriter.flush();
        jsonWriter.close();
    }

    public static void readJSON() throws FileNotFoundException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        JsonReader jsonReader = new JsonReader(new FileReader("out.json"));
        Test test = gson.fromJson(jsonReader, Test.class);

        System.out.println(test);

    }

}

