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

import java.util.List;

@Service
@AllArgsConstructor
public class AdminService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Employee getAdminById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public void saveAdmin(Employee employee) {
        employeeRepository.save(employee);
    }

    public void deleteAdminById(Long id) {
        employeeRepository.deleteById(id);
    }

    public String addEmployee(Employee employee){
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employeeRepository.save(employee);
        return "user added to system";
    }


/*
    public boolean isValidLogin(String email, String password) {
        List<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees) {

            if (email.equals(employee.getEmail()) && password.equals(employee.getPassword())) {
                return true;
            }
        }
        return false;

    }

 */

}