package com.diginamic.gt.controller;

import com.diginamic.gt.dto.EmployeeTask;
import com.diginamic.gt.entities.Employee;
import com.diginamic.gt.exceptions.BadRequestException;
import com.diginamic.gt.services.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "employee", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public List<Employee> create(@RequestBody Employee employee) throws BadRequestException {
        employeeService.create(employee);
        return this.getAll();
    }

    @GetMapping
    public List<Employee> getAll() {
        return employeeService.search(null);
    }

    @GetMapping(value = "/{id}")
    public Employee getById(@PathVariable int id) {
        return employeeService.read(id);
    }

    @PutMapping(value = "/{id}")
    public List<Employee> update(@PathVariable int id, @RequestBody Employee employee) {
        if (id == employee.getId()) {
            employeeService.update(employee);
        }
        return this.getAll();
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping(value = "/{id}/task")
    public Employee attachTask(@Validated @RequestBody EmployeeTask employeeTask) throws BadRequestException {
        /*if (idEmployee == employeeTask.getIdEmployee()) {
            throw new BadRequestException("l id d employe ne correspond pas", "REQUEST_INCONSISTENCY");
        }*/
        employeeService.attachTaskToUser(employeeTask.getIdTask(), employeeTask.getIdEmployee());

        return employeeService.read(employeeTask.getIdEmployee());
    }


    @DeleteMapping(value = "/{id}")
    public List<Employee> deleteById(@PathVariable int id) {
        employeeService.delete(id);
        return this.getAll();
    }
}
