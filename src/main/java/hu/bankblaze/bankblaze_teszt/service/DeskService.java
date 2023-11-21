package hu.bankblaze.bankblaze_teszt.service;

import hu.bankblaze.bankblaze_teszt.model.Desk;
import hu.bankblaze.bankblaze_teszt.model.Employee;
import hu.bankblaze.bankblaze_teszt.repo.DeskRepository;
import hu.bankblaze.bankblaze_teszt.repo.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DeskService {
    private DeskRepository deskRepository;
    private EmployeeRepository employeeRepository;

    public void saveDeskLayout(Desk desk) {
        deskRepository.save(desk);
    }
    public Long getDeskIdByLoggedInUser(Long loggedInUserId) {
        // Az adott loggedInUserId-hez tartozó desk ID lekérdezése a Desk entitásból
        Desk desk = deskRepository.findByEmployeeId(loggedInUserId);
        if (desk != null) {
            return desk.getId();
        }
        return null;
    }

}
