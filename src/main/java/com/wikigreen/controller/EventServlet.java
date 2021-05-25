package com.wikigreen.controller;

import com.wikigreen.controller.json.io.EventReader;
import com.wikigreen.controller.json.io.EventReaderImpl;
import com.wikigreen.controller.json.io.EventWriter;
import com.wikigreen.controller.json.io.EventWriterImpl;
import com.wikigreen.dao.DaoException;
import com.wikigreen.dto.DtoUtils;
import com.wikigreen.dto.EventDTO;
import com.wikigreen.model.Event;
import com.wikigreen.service.EventService;
import com.wikigreen.service.implementations.EventServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class EventServlet extends HttpServlet {
    public static final DtoUtils DTO_UTILS = new DtoUtils();
    private EventService eventService = new EventServiceImpl();
    private EventReader reader;
    private EventWriter writer;
    private static final String JSON_CONTENT_TYPE = "application/json";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        reader = new EventReaderImpl(req.getReader());
        writer = new EventWriterImpl(resp.getWriter());

        resp.setContentType(JSON_CONTENT_TYPE);

        EventDTO eventDTO = reader.read();
        if (eventDTO.getFile() != null && eventDTO.getFile().getId() == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writer.writeEmpty();
            return;
        }

        Event event;

        try {
            event = eventService.save(DTO_UTILS.convertEventDTOToEvent(eventDTO));
            event = eventService.getById(event.getId());
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writer.writeEmpty();
            return;
        }

        resp.setStatus(HttpServletResponse.SC_CREATED);
        writer.write(DTO_UTILS.convertEventToEventDTO(event));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        writer = new EventWriterImpl(resp.getWriter());
        String pathInfo = req.getPathInfo();

        resp.setContentType(JSON_CONTENT_TYPE);
        if (pathInfo == null || "/".equals(pathInfo)) {
            List<Event> eventList = eventService.getAll();
            List<EventDTO> eventDTOs = eventList.stream()
                    .map(DTO_UTILS::convertEventToEventDTO)
                    .collect(Collectors.toList());

            writer.writeList(eventDTOs);
            return;
        } else if (!pathInfo.matches("/\\d+$")) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writer.writeEmpty();
            return;
        }

        Long id = Long.parseLong(pathInfo.substring(1));

        try {
            Event event = eventService.getById(id);
            writer.write(DTO_UTILS.convertEventToEventDTO(event));
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writer.writeEmpty();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        reader = new EventReaderImpl(req.getReader());
        writer = new EventWriterImpl(resp.getWriter());

        resp.setContentType(JSON_CONTENT_TYPE);

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || !pathInfo.matches("/\\d+$")) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writer.writeEmpty();
            return;
        }

        EventDTO eventDTO = reader.read();
        eventDTO.setId(Long.parseLong(pathInfo.substring(1)));

        Event event;
        try {
            event = eventService.update(DTO_UTILS.convertEventDTOToEvent(eventDTO));
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writer.writeEmpty();
            return;
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        writer.write(DTO_UTILS.convertEventToEventDTO(event));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo.length() < 1) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        long id;
        try {
            id = Long.parseLong(pathInfo.substring(1));
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            eventService.deleteById(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
