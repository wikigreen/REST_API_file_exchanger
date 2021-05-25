package com.wikigreen.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "event_type")
    @Enumerated(value = EnumType.STRING)
    private EventType eventType;

    @Column(name = "event_date")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date date;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id",referencedColumnName="id")
    private File file;

    public Event() {
    }

    public Event(EventType eventType, Date date, File file) {
        this.eventType = eventType;
        this.date = date;
        this.file = file;
    }

    public Event(Long id, EventType eventType, Date date, File file) {
        this.id = id;
        this.eventType = eventType;
        this.date = date;
        this.file = file;
    }

    public Event(EventType eventType, File file) {
        this.eventType = eventType;
        this.file = file;
        this.date = new Date();
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

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (!Objects.equals(id, event.id)) return false;
        if (eventType != event.eventType) return false;
        if (!Objects.equals(date, event.date)) return false;
        return Objects.equals(file, event.file);
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
        return "Event{" +
                "id=" + id +
                ", eventType=" + eventType +
                ", date=" + date +
                ", file id=" + file.getId() +
                '}';
    }
}
