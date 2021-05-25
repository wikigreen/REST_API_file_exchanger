package com.wikigreen;

import com.wikigreen.dao.DaoException;
import com.wikigreen.dto.*;
import com.wikigreen.model.*;
import com.wikigreen.service.*;
import com.wikigreen.service.implementations.EventServiceImpl;
import com.wikigreen.service.implementations.FileServiceImpl;
import com.wikigreen.service.implementations.UserServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestingDAO {
    public static void main(String[] args) throws DaoException, IOException {
        UserService userService = new UserServiceImpl();
        Account account = new Account();
        account.setId(12468L);
        userService.save(new User(
                account,
                new ArrayList<>(),
                new ArrayList<>(),
                "fir",
                "las"
        ));
    }

    public static void createEventExistingFile() throws DaoException {
        EventService service = new EventServiceImpl();

        File file = new File();
        file.setId(3L);

        service.save(new Event(
                EventType.DOWNLOADING, file
        ));
    }

    public static void createFileExistingUser() throws DaoException {
        FileService service = new FileServiceImpl();

        User user = new User();
        user.setId(5L);

        File file = new File(
                "Filename", 10000L, user
        );

        service.save(file);
    }

    public static void createUserWithAllEntities() throws DaoException {
        new UserServiceImpl().save(createUserEntity());
    }

    public static void createUserAndFillItWithFiles() throws DaoException {
        createUser();
        User user = getUser(2L);
        addFiles(user);

        System.out.println("User " + user.getId() + " files: " + new FileServiceImpl().getAll());

        System.out.println("===================================");

        System.out.println("User after adding files: " + getUser(1L));
    }

    public static void createUser() throws DaoException {
        Account account = new Account(null, "john@email.com", "second2", AccountStatus.ACTIVE);
        User user = new User(account, new ArrayList<>(), new ArrayList<>(), "John", "Smyth");
        account.setUser(user);

        UserService service = new UserServiceImpl();

        service.save(user);
    }

    public static void readUser(Long id){
        UserService service = new UserServiceImpl();

        try {
            System.out.println(service.getById(id));
        } catch (DaoException e) {
            System.out.println(e.getMessage());
        }
    }

    public static User getUser(Long id) throws DaoException {
        UserService service = new UserServiceImpl();

        return service.getById(id);
    }

    public static String test(){
        try{
            return "return";
        } finally {
            System.out.println("Finally");
        }
    }

    public static void addFiles(User owner) throws DaoException {
        File[] files = new File[]{
                new File("FileJohn1.txt", 1024000L, owner),
                new File("FileJohn2.txt", 1024000L, owner),
                new File("FileJohn3.txt", 1024000L, owner),
                new File("FileJohn4.txt", 1024000L, owner),
                new File("FileJohn5.txt", 1024000L, owner)
        };

        FileService fileService = new FileServiceImpl();

        for (File file: files){
            System.out.println("Added new file " + fileService.save(file));
        }
    }

    public static void addEvents(File... files) throws InterruptedException, DaoException {
        List<Event> events = new ArrayList<>();

        for(File file: files){
            Thread.sleep(1000L);
            events.add(new Event(EventType.CREATION, file));
            Thread.sleep(1000L);
            events.add(new Event(EventType.DOWNLOADING, file));
            System.out.println("Still working....");
            Thread.sleep(1000L);
            events.add(new Event(EventType.UPDATE, file));
            Thread.sleep(1000L);
            events.add(new Event(EventType.DOWNLOADING, file));
            System.out.println("Still working....");
        }

        System.out.println("Done!!!");
        EventService eventService = new EventServiceImpl();
        for(Event event: events){
            eventService.save(event);
        }
    }

    public static User createUserEntity() {
        User user = new User(
                new Account(null, "rick_sanchez@gmail.com", "rick", AccountStatus.ACTIVE),
                new ArrayList<>(),
                new ArrayList<>(),
                "Rick", "Sanchez"
        );

        user.getAccount().setUser(user);
        user.setFiles(List.of(
                new File("RickFile1.txt", 1024000L, user),
                new File("RickFile2.txt", 1024000L, user),
                new File("RickFile3.txt", 1024000L, user),
                new File("RickFile4.txt", 1024000L, user)
        ));

        user.setEvents(List.of(
                new Event(EventType.CREATION, user.getFiles().get(0)),
                new Event(EventType.DOWNLOADING, user.getFiles().get(0)),
                new Event(EventType.UPDATE, user.getFiles().get(0)),
                new Event(EventType.CREATION, user.getFiles().get(1)),
                new Event(EventType.DOWNLOADING, user.getFiles().get(1)),
                new Event(EventType.UPDATE, user.getFiles().get(1)),
                new Event(EventType.CREATION, user.getFiles().get(2)),
                new Event(EventType.DOWNLOADING, user.getFiles().get(2)),
                new Event(EventType.UPDATE, user.getFiles().get(2)),
                new Event(EventType.CREATION, user.getFiles().get(3)),
                new Event(EventType.DOWNLOADING, user.getFiles().get(3)),
                new Event(EventType.UPDATE, user.getFiles().get(3))
        ));

        return user;
    }

    public static UserDTO createUserDtoEntity() {
        UserDTO user = new UserDTO(
                new AccountDTO(null, "post_man@gmail.com", "postmaaan", AccountStatus.ACTIVE),
                new ArrayList<>(),
                new ArrayList<>(),
                "Post", "CreateAccount"
        );

        user.getAccountDTO().setUserDTO(user);
        user.setFiles(List.of(
                new FileDTO("postman_account1.txt", 1024000L, user),
                new FileDTO("postman_account2.txt", 1024000L, user),
                new FileDTO("postman_account3.txt", 1024000L, user),
                new FileDTO("postman_account4.txt", 1024000L, user)
        ));

        user.setEvents(List.of(
                new EventDTO(EventType.CREATION, user.getFiles().get(0)),
                new EventDTO(EventType.DOWNLOADING, user.getFiles().get(0)),
                new EventDTO(EventType.UPDATE, user.getFiles().get(0)),
                new EventDTO(EventType.CREATION, user.getFiles().get(1)),
                new EventDTO(EventType.DOWNLOADING, user.getFiles().get(1)),
                new EventDTO(EventType.UPDATE, user.getFiles().get(1)),
                new EventDTO(EventType.CREATION, user.getFiles().get(2)),
                new EventDTO(EventType.DOWNLOADING, user.getFiles().get(2)),
                new EventDTO(EventType.UPDATE, user.getFiles().get(2)),
                new EventDTO(EventType.CREATION, user.getFiles().get(3)),
                new EventDTO(EventType.DOWNLOADING, user.getFiles().get(3)),
                new EventDTO(EventType.UPDATE, user.getFiles().get(3))
        ));

        return user;
    }
}

