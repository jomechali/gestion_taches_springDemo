package com.diginamic.gt.services.implementations;

import com.diginamic.gt.entities.Employee;
import com.diginamic.gt.entities.Task;
import com.diginamic.gt.repository.TaskRepository;
import com.diginamic.gt.services.TaskService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class TaskServiceImplementation implements TaskService {

    private TaskRepository taskRepository;

    public TaskServiceImplementation(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * @return
     */
    @Override
    public List<Task> search() {
        return taskRepository.findAll();
    }

    /**
     * @param task
     */
    @Override
    public void create(Task task) {
        taskRepository.save(task);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Task read(int id) {
        return taskRepository.findById(id).orElse(null);
    }

    /**
     * @param task
     * @return
     */
    @Override
    public Task update(Task task) {
        Task currentTask = this.read(task.getId());
        currentTask.setRt(task.getRt());
        // check validity of the dates; business constraints
        return currentTask;
    }

    /**
     * @param id
     */
    @Override
    public void deleteByID(int id) {
        taskRepository.deleteById(id);
    }

}
