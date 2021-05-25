package com.wikigreen.dto;

import com.wikigreen.model.EventType;

import java.util.Date;
import java.util.Objects;

public class EventDTO {
    private Long id;
    private EventType eventType;
    private Date date;
    private FileDTO file;

    public EventDTO(Long id, EventType eventType, Date date, FileDTO file) {
        this.id = id;
        this.eventType = eventType;
        this.date = date;
        this.file = file;
    }

    public EventDTO(EventType eventType, Date date, FileDTO file) {
        this.eventType = eventType;
        this.date = date;
        this.file = file;
    }

    public EventDTO() {}

    public EventDTO(EventType eventType, FileDTO file) {
        this.eventType = eventType;
        this.date = new Date();
        this.file = file;
    }

    public EventDTO(Long id, EventType eventType, FileDTO file) {
        this.id = id;
        this.eventType = eventType;
        this.date = new Date();
        this.file = file;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public FileDTO getFile() {
        return file;
    }

    public void setFile(FileDTO file) {
        this.file = file;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventDTO eventDTO = (EventDTO) o;

        if (!Objects.equals(id, eventDTO.id)) return false;
        if (eventType != eventDTO.eventType) return false;
        if (!Objects.equals(date, eventDTO.date)) return false;
        return Objects.equals(file, eventDTO.file);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (eventType != null ? eventType.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (file != null ? file.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EventDTO{" +
                "id=" + id +
                ", eventType=" + eventType +
                ", date=" + date +
                ", file=" + file +
                '}';
    }
}
