package hu.bankblaze.bankblaze_teszt.service;

import hu.bankblaze.bankblaze_teszt.model.Desk;
import hu.bankblaze.bankblaze_teszt.model.Employee;
import hu.bankblaze.bankblaze_teszt.model.QueueNumber;
import hu.bankblaze.bankblaze_teszt.repo.DeskRepository;
import hu.bankblaze.bankblaze_teszt.repo.EmployeeRepository;
import hu.bankblaze.bankblaze_teszt.repo.QueueNumberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DeskService {
    private DeskRepository deskRepository;
    private EmployeeRepository employeeRepository;
    private QueueNumberRepository queueNumberRepository;

    private List<Desk> desks;

    public void saveDeskLayout(Desk desk) {
        deskRepository.save(desk);
    }

    public Long getDeskIdByLoggedInUser(Long loggedInUserId) {
        // Az adott loggedInUserId-hez tartozó desk ID lekérdezése a Desk entitásból
        Desk desk = deskRepository.findByEmployeeId(loggedInUserId);

        return desk.getId();
    }

    public Long getDeskIdByActiveUser() {
        for (Desk desk : desks) {
            if (desk.getEmployee() != null) {
                return desk.getId();
            }
        } return null;
    }
    public Desk getDeskByEmployeeId(Long employeeId) {
        return deskRepository.findByEmployeeId(employeeId);
    }

    public void saveDesk(Desk desk) {
        deskRepository.save(desk);
    }

    public void assignDeskAndQueueNumber(Long employeeId, Long deskId, Long queueNumberId) {
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        Optional<Desk> deskOptional = deskRepository.findById(deskId);
        Optional<QueueNumber> queueNumberOptional = queueNumberRepository.findById(queueNumberId);

        if (employeeOptional.isPresent() && deskOptional.isPresent() && queueNumberOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            Desk desk = deskOptional.get();
            QueueNumber queueNumber = queueNumberOptional.get();

            employee.setDesk(desk);
            employee.setQueueNumber(queueNumber);

            desk.setQueueNumber(queueNumber.getNumber());

            deskRepository.save(desk);

        } else {
            throw new EntityNotFoundException("Nem található entitás valamelyik azonosító alapján.");

        }
    }

}
