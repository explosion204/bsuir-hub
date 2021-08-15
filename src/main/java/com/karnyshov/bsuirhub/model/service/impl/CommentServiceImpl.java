package com.karnyshov.bsuirhub.model.service.impl;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.dao.CommentDao;
import com.karnyshov.bsuirhub.model.entity.Comment;
import com.karnyshov.bsuirhub.model.service.AssignmentService;
import com.karnyshov.bsuirhub.model.service.CommentService;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;
import java.util.Optional;

/**
 * {@code CommentServiceImpl} class is an implementation of {@link CommentService} interface.
 * @author Dmitry Karnyshov
 */
@Named
public class CommentServiceImpl implements CommentService {
    @Inject
    private CommentDao commentDao;

    @Override
    public Optional<Comment> findById(long id) throws ServiceException {
        try {
            return commentDao.selectById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public long findByGrade(int start, int size, long gradeId, List<Comment> result) throws ServiceException {
        try {
            commentDao.selectByGrade(start, size, gradeId, result);
            return commentDao.selectCountByGrade(gradeId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public long create(Comment comment) throws ServiceException {
        try {
            return commentDao.insert(comment);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Comment comment) throws ServiceException {
        try {
            commentDao.update(comment);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(long id) throws ServiceException {
        try {
            commentDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
