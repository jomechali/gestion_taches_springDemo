package com.diginamic.gt.services.implementations;

import com.diginamic.gt.entities.Employee;
import com.diginamic.gt.entities.Task;
import com.diginamic.gt.exceptions.BadRequestException;
import com.diginamic.gt.repository.EmployeeRepository;
import com.diginamic.gt.services.EmployeeService;
import com.diginamic.gt.services.TaskService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeServiceImplementation implements EmployeeService {

    private EmployeeRepository employeeRepository;
    private TaskService taskService;

    public EmployeeServiceImplementation(EmployeeRepository employeeRepository, TaskService taskService) {
        this.employeeRepository = employeeRepository;
        this.taskService = taskService;
    }

    @Override
    public List<Employee> search(String query) {
        return query == null ? employeeRepository.findAll() : null;
    }

    @Override
    public void create(Employee employee) throws BadRequestException {

        Optional<Employee> employee1 = employeeRepository.findByUserName(employee.getUsername());
        if (employee1.isEmpty()) {
            employeeRepository.save(employee);
            return;
        }
        throw new BadRequestException("l utilisateur existe deja", "ERR_MAIL_EXISTS");
    }

    @Override
    public Employee read(int id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    public Employee update(Employee employee) {
        Employee currentEmployee = this.read(employee.getId());
        currentEmployee.setFirstName(employee.getFirstName());
        currentEmployee.setLastName(employee.getLastName());
        return currentEmployee;
    }

    /**
     * @param name
     * @return
     */
    @Override
    public Employee findByUserName(String name) {
        /*return employeeRepository.findByUserName(name).orElse(null);*/
        return employeeRepository.search(name).findFirst().orElse(null);
    }

    @Override
    public void delete(int id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public void attachTaskToUser(int idTask, int idEmployee) throws BadRequestException {
        // find
        Task currentTask = taskService.read(idTask);
        Employee currentEmployee = this.read(idEmployee);
        // if not exists throws an exception, but should be done in the read functions
        if (currentEmployee == null || currentTask == null) {
            throw new BadRequestException("L employe ou la tache n existe pas", "NOT_EXISTS");
        }

        List<Task> tasks;
        if (currentEmployee.getTasks() == null) {
            tasks = new ArrayList<>(); // in fact can be null thanks to the new in the constructor
        } else {
            Optional<Task> existingTask = currentEmployee.getTasks().stream().filter(userTask -> userTask.getId() == idEmployee).findFirst();
            if (existingTask.isPresent()) {
                throw new BadRequestException("tache deja attribuee a cet employee", "ALREADY_ATTACHED");
            }
        }

        // register the data
        currentEmployee.getTasks().add(currentTask);
    }
}
