package hu.bankblaze.bankblaze_teszt.repo;

import hu.bankblaze.bankblaze_teszt.model.Employee;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {


    Employee findByEmail(String email);

    Employee findByPassword(String password);


    Optional<Employee> findByName(String name);

}