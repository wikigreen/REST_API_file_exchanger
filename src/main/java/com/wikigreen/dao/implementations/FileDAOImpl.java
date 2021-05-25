package com.wikigreen.dao.implementations;

import com.wikigreen.dao.FileDAO;
import com.wikigreen.model.Account;
import com.wikigreen.model.Event;
import com.wikigreen.model.File;
import com.wikigreen.model.User;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileDAOImpl extends SessionAndTransactionAccess implements FileDAO {
    @Override
    public Long save(File file) {
        if(file.getOwner() != null && file.getOwner().getId() != null){
            file.setOwner(
                currentSession.load(User.class, file.getOwner().getId())
            );
        }
        file.setUploadDate(new Date());
        file.setUpdateDate(file.getUploadDate());
        return (Long) currentSession.save(file);
    }

    @Override
    public void update(File file) {
        File preUpdFile = currentSession.load(File.class, file.getId());

        if(file.getOwner() != null) {
            preUpdFile.setOwner(file.getOwner());
        }

        if(file.getFileName() != null) {
            preUpdFile.setFileName(file.getFileName());
        }

        if (file.getFileSizeInBits() != null){
            preUpdFile.setFileSizeInBits(file.getFileSizeInBits());
        }

        if (file.getStatus() != null) {
            preUpdFile.setStatus(file.getStatus());
        }

        preUpdFile.setUpdateDate(new Date());
        currentSession.update(preUpdFile);
    }

    @Override
    public File getById(Long id){
        try {
            File file = currentSession.createQuery(
                    "SELECT f FROM File f " +
                            "LEFT JOIN FETCH f.owner " +
                            "WHERE f.id = :id", File.class)
                    .setParameter("id", id)
                    .getSingleResult();

            initializeFile(file);

            return file;
        } catch (NoResultException e){
            return null;
        }
    }

    @Override
    public void deleteById(Long id){
        File file = new File();
        file.setId(id);
        currentSession.delete(file);
    }

    @Override
    public List<File> getAll() {
        List<File> resultList = currentSession.createQuery(
                "SELECT f FROM File f " +
                        "LEFT JOIN FETCH f.owner",
                File.class).getResultList();

        resultList.forEach(this::initializeFile);

        return resultList;
    }

    private void initializeFile(File file) {
        EventDAOImpl.initializeFile(file);
    }
}