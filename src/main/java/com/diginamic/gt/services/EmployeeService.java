package com.diginamic.gt.services;

import com.diginamic.gt.entities.Employee;
import com.diginamic.gt.exceptions.BadRequestException;

import java.util.List;

public interface EmployeeService {
    List<Employee> search(String query);

    void create(Employee employee) throws BadRequestException;

    Employee read(int id);

    Employee findByUserName(String name);

    Employee update(Employee employee);

    void delete(int id);

    void attachTaskToUser(int task, int employee) throws BadRequestException;
}
