package com.wikigreen.dto;


import com.wikigreen.model.Account;
import com.wikigreen.model.Event;
import com.wikigreen.model.File;
import com.wikigreen.model.User;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class DtoUtils {
    public UserDTO convertUserToUserDTO(User user) {
        if (user == null) return null;
        if (user.getFiles() == null) {
            user.setFiles(new ArrayList<>());
        }
        if (user.getEvents() == null) {
            user.setEvents(new ArrayList<>());
        }

        UserDTO userDTO = new UserDTO(
                user.getId(),
                null,
                new ArrayList<>(),
                new ArrayList<>(),
                user.getFirstName(),
                user.getLastName()
        );

        AccountDTO rawAccountDTO = null;

        if (user.getAccount() != null) {
            rawAccountDTO = new AccountDTO(
                    user.getAccount().getId(),
                    null,
                    user.getAccount().getEmail(),
                    user.getAccount().getNickName(),
                    user.getAccount().getAccountStatus()
            );
            rawAccountDTO.setUserDTO(userDTO);
        }

        userDTO.setAccount(rawAccountDTO);


        userDTO.setFiles(
                user.getFiles().stream()
                        .map(file ->
                                new FileDTO(
                                        file.getId(),
                                        file.getFileName(),
                                        file.getFileSizeInBits(),
                                        userDTO,
                                        file.getStatus(),
                                        file.getUploadDate(),
                                        file.getUpdateDate()
                                )
                        )
                        .collect(Collectors.toList())
        );

        userDTO.setEvents(
                user.getEvents().stream()
                        .map(event -> {
                                    if (event.getFile() == null){
                                        return new EventDTO(
                                                event.getId(),
                                                event.getEventType(),
                                                event.getDate(),
                                                null
                                        );
                                    }

                                    return new EventDTO(
                                            event.getId(),
                                            event.getEventType(),
                                            event.getDate(),
                                            userDTO.getFiles().stream()
                                                    .filter(fileDTO -> fileDTO.getId().equals(event.getFile().getId()))
                                                    .findFirst().orElseThrow(IllegalStateException::new)
                                    );
                                }

                        )
                        .collect(Collectors.toList())
        );
        return userDTO;
    }

    public AccountDTO convertAccountToAccountDTO(Account account) {
        if (account == null) return null;

        if (account.getUser() == null) {
            return new AccountDTO(
                    account.getId(),
                    null,
                    account.getEmail(),
                    account.getNickName(),
                    account.getAccountStatus()
            );
        }

        return convertUserToUserDTO(account.getUser()).getAccountDTO();
    }

    public FileDTO convertFileToFileDTO(File file) {
        if (file == null) return null;

        return new FileDTO(
            file.getId(),
            file.getFileName(),
            file.getFileSizeInBits(),
            convertUserToUserDTO(file.getOwner()),
            file.getStatus(),
            file.getUploadDate(),
            file.getUpdateDate()
        );
    }

    public EventDTO convertEventToEventDTO(Event event) {
        if (event == null) return null;

        return new EventDTO(
            event.getId(),
            event.getEventType(),
            event.getDate(),
            convertFileToFileDTO(event.getFile())
        );
    }

    public User convertUserDTOtoUser(UserDTO userDTO) {
        if (userDTO == null) return null;
        if (userDTO.getFiles() == null) {
            userDTO.setFiles(new ArrayList<>());
        }
        if (userDTO.getEvents() == null) {
            userDTO.setEvents(new ArrayList<>());
        }

        User user = new User(
                userDTO.getId(),
                null,
                new ArrayList<>(),
                new ArrayList<>(),
                userDTO.getFirstName(),
                userDTO.getLastName()
        );

        Account rawAccount = null;

        if (userDTO.getAccountDTO() != null) {
            rawAccount = new Account(
                    userDTO.getAccountDTO().getId(),
                    null,
                    userDTO.getAccountDTO().getEmail(),
                    userDTO.getAccountDTO().getNickName(),
                    userDTO.getAccountDTO().getAccountStatus()
            );
            rawAccount.setUser(user);
        }

        user.setAccount(rawAccount);


        user.setFiles(
                userDTO.getFiles().stream()
                        .map(file ->
                                new File(
                                        file.getId(),
                                        file.getFileName(),
                                        file.getFileSizeInBits(),
                                        user,
                                        file.getStatus(),
                                        file.getUploadDate(),
                                        file.getUpdateDate()
                                )
                        )
                        .collect(Collectors.toList())
        );

        user.setEvents(
                userDTO.getEvents().stream()
                        .map(event -> {
                                    Event event1 = new Event(
                                            event.getId(),
                                            event.getEventType(),
                                            event.getDate(),
                                            null
                                    );

                                    if (event.getFile() == null){
                                        event1.setFile(null);
                                    } else if (event.getFile().getId() == null) {
                                        File file = new File(
                                                event.getFile().getFileName(),
                                                event.getFile().getFileSizeInBits(),
                                                user
                                        );

                                        event1.setFile(file);
                                    } else {
                                        event1.setFile(user.getFiles().stream()
                                                .filter(fileDTO -> fileDTO.getId().equals(event.getFile().getId()))
                                                .findFirst().orElseThrow(IllegalStateException::new));
                                    }
                                    return event1;
                                }

                        )
                        .collect(Collectors.toList())
        );
        return user;
    }
    public Account convertAccountDtoToAccount(AccountDTO accountDTO){
        if (accountDTO == null) return null;

        if (accountDTO.getUser() == null) {
            return new Account(
                    accountDTO.getId(),
                    null,
                    accountDTO.getEmail(),
                    accountDTO.getNickName(),
                    accountDTO.getAccountStatus()
            );
        } else {
            accountDTO.getUser().setAccount(accountDTO);
        }

        return convertUserDTOtoUser(accountDTO.getUser()).getAccount();
    }

    public File convertFileDTOToFile(FileDTO fileDTO) {
        if (fileDTO == null) return null;

        User user = null;

        if (fileDTO.getOwner() != null) {
            user = new User();
            user.setId(fileDTO.getOwner().getId());
        }

        return new File(
                fileDTO.getId(),
                fileDTO.getFileName(),
                fileDTO.getFileSizeInBits(),
                user,
                fileDTO.getStatus(),
                fileDTO.getUploadDate(),
                fileDTO.getUpdateDate()
        );
    }

    public Event convertEventDTOToEvent(EventDTO eventDTO) {
        if (eventDTO == null) return null;

        File file = null;

        if (eventDTO.getFile() != null) {
            file = new File();
            file.setId(eventDTO.getFile().getId());
        }

        return new Event(
                eventDTO.getId(),
                eventDTO.getEventType(),
                eventDTO.getDate(),
                file
        );
    }
}
