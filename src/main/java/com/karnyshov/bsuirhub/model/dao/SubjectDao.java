package com.karnyshov.bsuirhub.model.dao;

import com.karnyshov.bsuirhub.exception.DaoException;
import com.karnyshov.bsuirhub.model.entity.Subject;

import java.util.List;


/**
 * {@code SubjectDao} interface extends {@link BaseDao}. It provides a bunch of means for manipulating stored
 * {@link Subject} entities.
 * @author Dmitry Karnyshov
 */
public interface SubjectDao extends BaseDao<Subject> {
     /**
      * Select {@code Subject} entities by specified name.
      *
      * @param offset amount of records that will be skipped from start in the query result.
      * @param limit amount of records that will appear in the query result.
      * @param keyword substring that the name of the subject can contain.
      * @param result {@link List} instance to hold entities retrieved from database.
      * @throws DaoException if an error occurred while processing the query.
      */
     void selectByName(int offset, int limit, String keyword, List<Subject> result) throws DaoException;

     /**
      * Select count of {@code Subject} entities with specified name.
      *
      * @param keyword substring that the name of the subject can contain.
      * @return {@code long} value that represents count of stored subjects with specified name.
      * @throws DaoException if an error occurred while processing the query.
      */
     long selectCountByName(String keyword) throws DaoException;

     /**
      * Select {@code Subject} entities by specified short name.
      *
      * @param offset amount of records that will be skipped from start in the query result.
      * @param limit amount of records that will appear in the query result.
      * @param keyword substring that the short name of the subject can contain.
      * @param result {@link List} instance to hold entities retrieved from database.
      * @throws DaoException if an error occurred while processing the query.
      */
     void selectByShortName(int offset, int limit, String keyword, List<Subject> result) throws DaoException;

     /**
      * Select count of {@code Subject} entities with specified short name.
      *
      * @param keyword substring that the short name of the subject can contain.
      * @return {@code long} value that represents count of stored subjects with specified short name.
      * @throws DaoException if an error occurred while processing the query.
      */
     long selectCountByShortName(String keyword) throws DaoException;
}
