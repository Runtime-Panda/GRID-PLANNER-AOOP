package com.gridplanner.interfaces;

import com.gridplanner.exception.GridException;
import java.util.List;
import java.util.Optional;

public interface GridRepository<T> {
    boolean save(T entity) throws GridException;
    List<T> findAll() throws GridException;
    Optional<T> findById(int id) throws GridException;
    boolean deleteById(int id) throws GridException;
}