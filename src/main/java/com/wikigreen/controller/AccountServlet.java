package com.wikigreen.controller;

import com.wikigreen.controller.json.io.AccountReaderImpl;
import com.wikigreen.controller.json.io.AccountWriter;
import com.wikigreen.controller.json.io.AccountWriterImpl;
import com.wikigreen.dao.DaoException;
import com.wikigreen.dto.AccountDTO;
import com.wikigreen.dto.DtoUtils;
import com.wikigreen.dto.UserDTO;
import com.wikigreen.model.Account;
import com.wikigreen.service.AccountService;
import com.wikigreen.service.implementations.AccountServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AccountServlet extends HttpServlet {
    public static final DtoUtils DTO_UTILS = new DtoUtils();
    private AccountService accountService = new AccountServiceImpl();
    private AccountReaderImpl reader;
    private AccountWriter writer;
    private static final String JSON_CONTENT_TYPE = "application/json";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        reader = new AccountReaderImpl(req.getReader());
        writer = new AccountWriterImpl(resp.getWriter());

        resp.setContentType(JSON_CONTENT_TYPE);


        AccountDTO accountDTO = reader.read();
        UserDTO userDTO = accountDTO.getUser();

        if (userDTO != null){
            userDTO.setAccount(accountDTO);
            if (userDTO.getFiles() != null){
                userDTO.getFiles().forEach(file -> file.setOwner(userDTO));
            }
            if (userDTO.getEvents() != null) {
                userDTO.getEvents().forEach(event -> event.getFile().setOwner(userDTO));
            }
        }
        Account account;

        try {
            account = accountService.save(DTO_UTILS.convertAccountDtoToAccount(accountDTO));
            account = accountService.getById(account.getId());
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writer.writeEmpty();
            return;
        }



        resp.setStatus(HttpServletResponse.SC_CREATED);
        writer.write(DTO_UTILS.convertAccountToAccountDTO(account));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        writer = new AccountWriterImpl(resp.getWriter());
        String pathInfo = req.getPathInfo();

        resp.setContentType(JSON_CONTENT_TYPE);
        if (pathInfo == null || "/".equals(pathInfo)) {
            List<Account> accounts = accountService.getAll();
            List<AccountDTO> accountDTOs = accounts.stream()
                    .map(DTO_UTILS::convertAccountToAccountDTO)
                    .collect(Collectors.toList());

            writer.writeList(accountDTOs);
            return;
        } else if (!pathInfo.matches("/\\d+$")) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writer.writeEmpty();
            return;
        }

        Long id = Long.parseLong(pathInfo.substring(1));
        try {
            Account account = accountService.getById(id);
            writer.write(DTO_UTILS.convertAccountToAccountDTO(account));
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writer.writeEmpty();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        reader = new AccountReaderImpl(req.getReader());
        writer = new AccountWriterImpl(resp.getWriter());

        resp.setContentType(JSON_CONTENT_TYPE);

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || !pathInfo.matches("/\\d+$")){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writer.writeEmpty();
            return;
        }

        AccountDTO accountDTO = reader.read();
        accountDTO.setId(Long.parseLong(pathInfo.substring(1)));

        Account account;
        try {
            account = accountService.update(DTO_UTILS.convertAccountDtoToAccount(accountDTO));
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writer.writeEmpty();
            return;
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        writer.write(DTO_UTILS.convertAccountToAccountDTO(account));
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
            accountService.deleteById(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (DaoException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
