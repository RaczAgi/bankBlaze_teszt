package hu.bankblaze.bankblaze_teszt.service;

import hu.bankblaze.bankblaze_teszt.model.Employee;
import hu.bankblaze.bankblaze_teszt.repo.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.List;
import java.util.Optional;
@Service
@AllArgsConstructor
public class AdminService {

    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Employee> getAllClerks() {
        return employeeRepository.getAllClerks();
    }

    public List<Employee> getAllAdmins() {
        return employeeRepository.getAllAdmins();
    }

    public Employee getAdminById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public void saveAdmin(Employee employee) {
        String encodedPassword = passwordEncoder.encode(employee.getPassword());
        employee.setPassword(encodedPassword);
        employeeRepository.save(employee);
    }

    public void deleteAdminById(Long id) {
        employeeRepository.deleteById(id);
    }


    public boolean checkLogin(String userName, String password) {
        // TODO Auto-generated method stub
        Optional<Employee> employee = employeeRepository.findByName(userName);
        if (employee.isPresent() && passwordEncoder.matches(password, employee.get().getPassword())) {
            return true;
        }
        return false;
    }


    public boolean isAdmin(String userName, String password) {
            Employee foundEmployee = employeeRepository.getAdminByName(userName);
        if (foundEmployee != null && foundEmployee.getRole().equals("ADMIN")) {
            return true;
        }

        return false;
    }
    public boolean isUser(String userName, String password) {
        Employee foundEmployee = employeeRepository.getAdminByName(userName);
        if (foundEmployee != null && foundEmployee.getRole().equals("USER")) {
            return true;
        }

        return false;
    }

    public void deleteAdminByName(String name) {
        Employee employee = employeeRepository.findByName(name).orElse(null);
        if (employee != null) {
            employeeRepository.delete(employee);
        }
    }

    public Employee getAdminByName(String name) {
        return employeeRepository.findByName(name).orElse(null);
    }
    public Long getLoggedInUserIdByUsername(String loggedInUsername) {
        Optional<Employee> employeeOptional = employeeRepository.findByName(loggedInUsername);
        return employeeOptional.map(Employee::getId).orElse(null);
    }

}

