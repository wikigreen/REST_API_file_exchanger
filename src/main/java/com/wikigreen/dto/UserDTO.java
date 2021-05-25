package com.wikigreen.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserDTO {
    private Long id;
    @SerializedName(value = "account")
    private AccountDTO accountDTO;
    private List<FileDTO> files;
    private List<EventDTO> events;
    private String firstName;
    private String lastName;

    public UserDTO(Long id, AccountDTO accountDTO, List<FileDTO> files, List<EventDTO> events, String firstName, String lastName) {
        this.id = id;
        this.accountDTO = accountDTO;
        this.files = files;
        this.events = events;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UserDTO(AccountDTO accountDTO, List<FileDTO> files, List<EventDTO> events, String firstName, String lastName) {
        this.accountDTO = accountDTO;
        this.files = files;
        this.events = events;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountDTO getAccountDTO() {
        return accountDTO;
    }

    public void setAccount(AccountDTO accountDTO) {
        this.accountDTO = accountDTO;
    }

    public List<FileDTO> getFiles() {
        return files;
    }

    public void setFiles(List<FileDTO> files) {
        this.files = files;
    }

    public List<EventDTO> getEvents() {
        return events;
    }

    public void setEvents(List<EventDTO> events) {
        this.events = events;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", account=" + accountDTO +
                ", fileIDs=" + files +
                ", events" + events +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
