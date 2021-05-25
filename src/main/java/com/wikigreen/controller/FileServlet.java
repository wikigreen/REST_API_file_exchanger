package com.wikigreen.controller;

import com.wikigreen.controller.json.io.*;
import com.wikigreen.dao.DaoException;
import com.wikigreen.dto.DtoUtils;
import com.wikigreen.dto.FileDTO;
import com.wikigreen.model.File;
import com.wikigreen.service.FileService;
import com.wikigreen.service.implementations.FileServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class FileServlet extends HttpServlet {
    public static final DtoUtils DTO_UTILS = new DtoUtils();
    private FileService fileService = new FileServiceImpl();
    private FileReaderImpl reader;
    private FileWriter writer;
    private static final String JSON_CONTENT_TYPE = "application/json";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        reader = new FileReaderImpl(req.getReader());
        writer = new FileWriterImpl(resp.getWriter());

        resp.setContentType(JSON_CONTENT_TYPE);

        FileDTO fileDTO = reader.read();
        if (fileDTO.getOwner() != null && fileDTO.getOwner().getId() == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writer.writeEmpty();
            return;
        }

        File file;

        try {
            file = fileService.save(DTO_UTILS.convertFileDTOToFile(fileDTO));
            file = fileService.getById(file.getId());
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writer.writeEmpty();
            return;
        }

        resp.setStatus(HttpServletResponse.SC_CREATED);
        writer.write(DTO_UTILS.convertFileToFileDTO(file));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        writer = new FileWriterImpl(resp.getWriter());
        String pathInfo = req.getPathInfo();

        resp.setContentType(JSON_CONTENT_TYPE);
        if (pathInfo == null || "/".equals(pathInfo)) {
            List<File> fileList = fileService.getAll();
            List<FileDTO> fileDTOs = fileList.stream()
                    .map(DTO_UTILS::convertFileToFileDTO)
                    .collect(Collectors.toList());

            writer.writeList(fileDTOs);
            return;
        } else if (!pathInfo.matches("/\\d+$")) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writer.writeEmpty();
            return;
        }

        Long id = Long.parseLong(pathInfo.substring(1));

        try {
            File file = fileService.getById(id);
            writer.write(DTO_UTILS.convertFileToFileDTO(file));
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writer.writeEmpty();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        reader = new FileReaderImpl(req.getReader());
        writer = new FileWriterImpl(resp.getWriter());

        resp.setContentType(JSON_CONTENT_TYPE);

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || !pathInfo.matches("/\\d+$")) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writer.writeEmpty();
            return;
        }

        FileDTO fileDTO = reader.read();
        fileDTO.setId(Long.parseLong(pathInfo.substring(1)));

        File file;
        try {
            file =fileService.update(DTO_UTILS.convertFileDTOToFile(fileDTO));
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writer.writeEmpty();
            return;
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        writer.write(DTO_UTILS.convertFileToFileDTO(file));
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
            fileService.deleteById(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
