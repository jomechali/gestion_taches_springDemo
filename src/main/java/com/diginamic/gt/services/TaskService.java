package com.diginamic.gt.services;

import com.diginamic.gt.entities.Employee;
import com.diginamic.gt.entities.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService {
    List<Task> search();

    void create(Task task);

    Task read(int id);

    /**
     * @param task
     * @return
     */
    Task update(Task task);

    /**
     * @param id
     */
    void deleteByID(int id);

}
