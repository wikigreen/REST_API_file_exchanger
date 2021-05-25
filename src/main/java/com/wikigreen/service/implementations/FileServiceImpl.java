package com.wikigreen.service.implementations;

import com.wikigreen.dao.FileDAO;
import com.wikigreen.dao.implementations.FileDAOImpl;
import com.wikigreen.model.File;
import com.wikigreen.service.FileService;
import com.wikigreen.dao.DaoException;
import org.hibernate.Session;

import javax.persistence.OptimisticLockException;
import java.util.List;

public class FileServiceImpl implements FileService {
    private static final FileDAO FILE_DAO = new FileDAOImpl();
    private static final String EXCEPTION_MESSAGE = "There is no file with id ";

    @Override
    public File save(File file) throws DaoException {
        FILE_DAO.openCurrentSessionWithTransaction();
        Long id = FILE_DAO.save(file);
        FILE_DAO.closeCurrentSessionWithTransaction();
        file.setId(id);
        return file;
    }

    @Override
    public File update(File file) throws DaoException {
        FILE_DAO.openCurrentSessionWithTransaction();
        File oldFile = FILE_DAO.getById(file.getId());

        try {
            if (oldFile == null) {
                throw new DaoException(EXCEPTION_MESSAGE + file.getId());
            }
        } finally {
            FILE_DAO.closeCurrentSessionWithTransaction();
        }

        FILE_DAO.openCurrentSessionWithTransaction();
        FILE_DAO.update(file);
        FILE_DAO.closeCurrentSessionWithTransaction();
        FILE_DAO.openCurrentSessionWithTransaction();
        try {
            return FILE_DAO.getById(file.getId());
        } finally {
            FILE_DAO.closeCurrentSessionWithTransaction();
        }
    }

    @Override
    public File getById(Long id) throws DaoException {
        FILE_DAO.openCurrentSessionWithTransaction();

        File file = FILE_DAO.getById(id);

        FILE_DAO.closeCurrentSessionWithTransaction();

        if (file == null) {
            throw new DaoException(EXCEPTION_MESSAGE + id);
        }

        return file;
    }

    @Override
    public void deleteById(Long id) throws DaoException {
        Session currentSession = FILE_DAO.openCurrentSessionWithTransaction();

        File file = new File();
        file.setId(id);

        FILE_DAO.deleteById(id);

        try {
            FILE_DAO.getCurrentTransaction().commit();
        } catch (OptimisticLockException e) {
            throw new DaoException(EXCEPTION_MESSAGE + id);
        } finally {
            currentSession.close();
        }
    }

    @Override
    public List<File> getAll() {
        FILE_DAO.openCurrentSessionWithTransaction();
        List<File> list = FILE_DAO.getAll();
        FILE_DAO.closeCurrentSessionWithTransaction();
        return list;
    }
}
