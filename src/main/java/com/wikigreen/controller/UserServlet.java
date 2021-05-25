package com.wikigreen.controller;

import com.wikigreen.controller.json.io.*;
import com.wikigreen.dao.DaoException;
import com.wikigreen.dto.AccountDTO;
import com.wikigreen.dto.DtoUtils;
import com.wikigreen.dto.UserDTO;
import com.wikigreen.model.User;
import com.wikigreen.service.UserService;
import com.wikigreen.service.implementations.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class UserServlet extends HttpServlet {
    public static final DtoUtils DTO_UTILS = new DtoUtils();
    private static final String JSON_CONTENT_TYPE = "application/json";
    private UserService userService = new UserServiceImpl();
    private UserReaderImpl reader;
    private UserWriter writer;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        reader = new UserReaderImpl(req.getReader());
        writer = new UserWriterImpl(resp.getWriter());

        resp.setContentType(JSON_CONTENT_TYPE);

        UserDTO userDTO = reader.read();
        AccountDTO accountDTO = userDTO.getAccountDTO();

        if (accountDTO != null) {
            accountDTO.setUserDTO(userDTO);
        }

        if (userDTO.getFiles() != null) {
            userDTO.getFiles().forEach(file -> file.setOwner(userDTO));
        }
        if (userDTO.getEvents() != null) {
            userDTO.getEvents().forEach(event -> event.getFile().setOwner(userDTO));
        }

        User user;

        try {
            user = userService.save(DTO_UTILS.convertUserDTOtoUser(userDTO));
            user = userService.getById(user.getId());
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writer.writeEmpty();
            return;
        }

        resp.setStatus(HttpServletResponse.SC_CREATED);
        writer.write(DTO_UTILS.convertUserToUserDTO(user));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        writer = new UserWriterImpl(resp.getWriter());
        String pathInfo = req.getPathInfo();

        resp.setContentType(JSON_CONTENT_TYPE);
        if (pathInfo == null || "/".equals(pathInfo)) {
            List<User> users = userService.getAll();
            List<UserDTO> userDTOs = users.stream()
                    .map(DTO_UTILS::convertUserToUserDTO)
                    .collect(Collectors.toList());

            writer.writeList(userDTOs);
            return;
        } else if (!pathInfo.matches("/\\d+$"))     {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writer.writeEmpty();
            return;
        }
        Long id = Long.parseLong(pathInfo.substring(1));

        try {
            User user = userService.getById(id);
            writer.write(DTO_UTILS.convertUserToUserDTO(user));
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writer.writeEmpty();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        reader = new UserReaderImpl(req.getReader());
        writer = new UserWriterImpl(resp.getWriter());

        resp.setContentType(JSON_CONTENT_TYPE);


        String pathInfo = req.getPathInfo();
        if (pathInfo == null || !pathInfo.matches("/\\d+$")){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writer.writeEmpty();
            return;
        }

        UserDTO userDTO = reader.read();
        userDTO.setId(Long.parseLong(pathInfo.substring(1)));

        User user;

        try {
            user = userService.update(DTO_UTILS.convertUserDTOtoUser(userDTO));
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writer.writeEmpty();
            return;
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        writer.write(DTO_UTILS.convertUserToUserDTO(user));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo.length() < 1) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        long id;

        try {
            id = Long.parseLong(pathInfo.substring(1));
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            userService.deleteById(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
