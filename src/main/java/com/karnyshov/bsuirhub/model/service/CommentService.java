package com.karnyshov.bsuirhub.model.service;

import com.karnyshov.bsuirhub.exception.ServiceException;
import com.karnyshov.bsuirhub.model.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<Comment> findById(long id) throws ServiceException;
    long findByGrade(int page, int pageSize, long gradeId, List<Comment> result) throws ServiceException;
    long create(Comment comment) throws ServiceException;
    void update(Comment comment) throws ServiceException;
    void delete(long id) throws ServiceException;
}
