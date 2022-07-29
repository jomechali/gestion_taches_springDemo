package com.diginamic.gt.controller;

import com.diginamic.gt.entities.Task;
import com.diginamic.gt.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "task")
public class TaskController {

    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@RequestBody Task task) {
        this.taskService.create(task);
    }

    @GetMapping
    public List<Task> getAll() {
        return taskService.search();
    }

    @GetMapping(path = "/{id}")
    public Task read(@PathVariable int id) {
        return taskService.read(id);
    }

    @PutMapping(path = "/{id}")
    public void update(@RequestBody Task task, @PathVariable int id) {
        if (task.getId() == id) {
            taskService.update(task);
        }
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable int id){
        taskService.deleteByID(id);
    }
}
