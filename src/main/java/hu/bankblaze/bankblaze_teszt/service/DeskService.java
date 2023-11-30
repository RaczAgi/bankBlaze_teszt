package hu.bankblaze.bankblaze_teszt.service;

import hu.bankblaze.bankblaze_teszt.model.Desk;
import hu.bankblaze.bankblaze_teszt.model.Employee;
import hu.bankblaze.bankblaze_teszt.model.Permission;
import hu.bankblaze.bankblaze_teszt.model.QueueNumber;
import hu.bankblaze.bankblaze_teszt.repo.DeskRepository;
import hu.bankblaze.bankblaze_teszt.repo.EmployeeRepository;
import hu.bankblaze.bankblaze_teszt.repo.QueueNumberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DeskService {
    private DeskRepository deskRepository;
    private QueueNumberService queueNumberService;
    private PermissionService permissionService;

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

    public void nextQueueNumber(Employee employee) {
        Desk desk = getDeskByEmployeeId(employee.getId());
        Permission permission = permissionService.getPermissionByEmployee(employee);
        List<QueueNumber> queueNumberList = new ArrayList<>();
        if (permission.getForRetail()){
            queueNumberList.add(queueNumberService.getNextRetail());
        } else if (permission.getForCorporate()){
            queueNumberList.add(queueNumberService.getNextCorporate());
        } else if (permission.getForTeller()){
            queueNumberList.add(queueNumberService.getNextTeller());
        } else if (permission.getForPremium()) {
            queueNumberList.add(queueNumberService.getNextPremium());
        }
        QueueNumber queueNumber = queueNumberService.getSmallestNumber(queueNumberList);
        desk.setQueueNumber(queueNumber);
        System.out.println(desk.getQueueNumber());
       deskRepository.save(desk);
    }
    public void clearQueueNumberInDeskTable(QueueNumber queueNumber) {

        Desk desk = findDeskByQueueNumber(queueNumber);


        if (desk != null) {

            desk.setQueueNumber(null);

            saveDesk(desk);
        }
    }

    protected Desk findDeskByQueueNumber(QueueNumber queueNumber) {
        return deskRepository.findByQueueNumber(queueNumber);
    }


}
