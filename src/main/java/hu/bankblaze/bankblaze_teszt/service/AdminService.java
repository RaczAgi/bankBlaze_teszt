package hu.bankblaze.bankblaze_teszt.service;
import hu.bankblaze.bankblaze_teszt.model.Desk;
import hu.bankblaze.bankblaze_teszt.model.Employee;
import hu.bankblaze.bankblaze_teszt.model.Permission;
import hu.bankblaze.bankblaze_teszt.model.QueueNumber;
import hu.bankblaze.bankblaze_teszt.repo.EmployeeRepository;
import hu.bankblaze.bankblaze_teszt.repo.PermissionRepository;
import hu.bankblaze.bankblaze_teszt.repo.QueueNumberRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;
@Service
@AllArgsConstructor
public class AdminService {

    private EmployeeRepository employeeRepository;
    private DeskService deskService;
    private PermissionService permissionService;
    private QueueNumberRepository queueNumberRepository;
    private PermissionRepository permissionRepository;

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

    public Employee getEmployeeByName(String name) {
        return employeeRepository.findByName(name).orElse(null);
    }


    public Long getLoggedInUserIdByUsername(String loggedInUsername) {
        Optional<Employee> employeeOptional = employeeRepository.findByName(loggedInUsername);
        return employeeOptional.map(Employee::getId).orElse(null);
    }

    public String getLoggedInClerks() {
        String loggedInUsername = getLoggedInUsername();
        Long loggedInUserId = getLoggedInUserIdByUsername(loggedInUsername);
        Long deskNumber = deskService.getDeskIdByLoggedInUser(loggedInUserId);
        String permission = permissionService.getPermissionByLoggedInUser(loggedInUserId);

        return "employee";
    }

    public String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public void setQueueCounts(Model model) {
        model.addAttribute("retailCount", queueNumberRepository.countByActiveTrueAndToRetailTrue());
        model.addAttribute("corporateCount", queueNumberRepository.countByActiveTrueAndToCorporateTrue());;
        model.addAttribute("tellerCount", queueNumberRepository.countByActiveTrueAndToTellerTrue());;
        model.addAttribute("privateCount", queueNumberRepository.countByActiveTrueAndToPremiumTrue());
    }

    public void setActualCount(Model model, Employee employee) {
        Permission permission = permissionService.getPermissionByEmployee(employee);
        Integer actualCount = 0;
        if (permission.getForRetail()) {
            actualCount = queueNumberRepository.countByActiveTrueAndToRetailTrue();
        } else if (permission.getForCorporate()) {
            actualCount = queueNumberRepository.countByActiveTrueAndToCorporateTrue();
        } else if (permission.getForTeller()) {
            actualCount = queueNumberRepository.countByActiveTrueAndToTellerTrue();
        } else if (permission.getForPremium()) {
            actualCount = queueNumberRepository.countByActiveTrueAndToPremiumTrue();
        }
        model.addAttribute("actualCount", actualCount);
    }

    public void setEmployeeCount(Model model, Employee employee) {
        Permission permission = permissionService.getPermissionByEmployee(employee);

        Integer employeeCount = 0;
        if (permission.getForRetail()) {
            employeeCount = permissionRepository.countByForRetailTrue();
        } else if (permission.getForCorporate()) {
            employeeCount = permissionRepository.countByForCorporateTrue();
        } else if (permission.getForTeller()) {
            employeeCount = permissionRepository.countByForTellerTrue();
        } else if (permission.getForPremium()) {
            employeeCount = permissionRepository.countByForPremiumTrue();
        }
        model.addAttribute("employeeCount", employeeCount);
    }
    public void processClosure(Model model) {
        String loggedInUsername = getLoggedInUsername();
        Long loggedInUserId = getLoggedInUserIdByUsername(loggedInUsername);
        Long deskNumber = deskService.getDeskIdByLoggedInUser(loggedInUserId);
        String permission = permissionService.getPermissionByLoggedInUser(loggedInUserId);
        Permission permissions = permissionService.getPermissions(loggedInUserId);

        QueueNumber nextQueueNumber = determineNextQueueNumber(permission, permissions);

        prepareModel(model, loggedInUsername, deskNumber, permission, nextQueueNumber);

        processNextQueueNumber(nextQueueNumber);
    }
    private QueueNumber determineNextQueueNumber(String permission, Permission permissions) {
        QueueNumber nextQueueNumber = null;
        if (permission != null) {
            if (Boolean.TRUE.equals(permissions.getForRetail())) {
                nextQueueNumber = queueNumberRepository.findFirstByActiveTrueAndToRetailTrue();
            } else if (Boolean.TRUE.equals(permissions.getForCorporate())) {
                nextQueueNumber = queueNumberRepository.findFirstByActiveTrueAndToCorporateTrue();
            } else if (Boolean.TRUE.equals(permissions.getForTeller())) {
                nextQueueNumber = queueNumberRepository.findFirstByActiveTrueAndToTellerTrue();
            } else if (Boolean.TRUE.equals(permissions.getForPremium())) {
                nextQueueNumber = queueNumberRepository.findFirstByActiveTrueAndToPremiumTrue();
            }
        }
        return nextQueueNumber;
    }
    private void prepareModel(Model model, String loggedInUsername, Long deskNumber, String permission, QueueNumber nextQueueNumber) {
        model.addAttribute("loggedInUsername", loggedInUsername);
        model.addAttribute("deskNumber", deskNumber);
        model.addAttribute("permission", permission);
        model.addAttribute("nextQueueNumber", nextQueueNumber);
        model.addAttribute("queueNumbers", queueNumberRepository.findAll());
    }

    private void processNextQueueNumber(QueueNumber nextQueueNumber) {
        if (nextQueueNumber != null) {
            nextQueueNumber.setActive(false);
            queueNumberRepository.save(nextQueueNumber);
        }
    }


    public void processRedirect(Model model, String redirectLocation) {
        String loggedInUsername = getLoggedInUsername();
        Long loggedInUserId = getLoggedInUserIdByUsername(loggedInUsername);
        Long deskNumber = deskService.getDeskIdByLoggedInUser(loggedInUserId);
        String permission = permissionService.getPermissionByLoggedInUser(loggedInUserId);
        Permission permissions = permissionService.getPermissions(loggedInUserId);

        QueueNumber nextQueueNumber = determineNextQueueNumber(permission, permissions);

        prepareModel(model, loggedInUsername, deskNumber, permission, nextQueueNumber);

        if (nextQueueNumber != null) {
            nextQueueNumber.setToRetail("retail".equals(redirectLocation));
            nextQueueNumber.setToCorporate("corporate".equals(redirectLocation));
            nextQueueNumber.setToTeller("teller".equals(redirectLocation));
            nextQueueNumber.setToPremium("premium".equals(redirectLocation));

            queueNumberRepository.save(nextQueueNumber);
        }
    }
    public void deleteNextQueueNumber(Model model) {
        String loggedInUsername = getLoggedInUsername();
        Long loggedInUserId = getLoggedInUserIdByUsername(loggedInUsername);
        Long deskNumber = deskService.getDeskIdByLoggedInUser(loggedInUserId);
        String permission = permissionService.getPermissionByLoggedInUser(loggedInUserId);
        Permission permissions = permissionService.getPermissions(loggedInUserId);

        QueueNumber nextQueueNumber = determineNextQueueNumber(permission, permissions);

        prepareModel(model, loggedInUsername, deskNumber, permission, nextQueueNumber);

        if (nextQueueNumber != null) {
            Desk desk = deskService.findDeskByQueueNumber(nextQueueNumber);
            if (desk != null) {
                desk.setQueueNumber(null);
                deskService.saveDesk(desk);
            }
            queueNumberRepository.delete(nextQueueNumber);
        }
    }
    public QueueNumber processNextClient(Model model, Employee employee) {
        String loggedInUsername = getLoggedInUsername();
        Long loggedInUserId = getLoggedInUserIdByUsername(loggedInUsername);
        Long deskNumber = deskService.getDeskIdByLoggedInUser(loggedInUserId);
        String permission = permissionService.getPermissionByLoggedInUser(loggedInUserId);
        Permission permissions = permissionService.getPermissions(loggedInUserId);

        QueueNumber nextQueueNumber = determineNextQueueNumber(permission, permissions);

        Permission actualPermission = permissionService.getPermissionByEmployee(employee);
        Integer actualCount = 0;
        if (actualPermission.getForRetail()) {
            actualCount = queueNumberRepository.countByActiveTrueAndToRetailTrue();
        } else if (actualPermission.getForCorporate()) {
            actualCount = queueNumberRepository.countByActiveTrueAndToCorporateTrue();
        } else if (actualPermission.getForTeller()) {
            actualCount = queueNumberRepository.countByActiveTrueAndToTellerTrue();
        } else if (actualPermission.getForPremium()) {
            actualCount = queueNumberRepository.countByActiveTrueAndToPremiumTrue();
        }
        model.addAttribute("actualCount", actualCount);
        Integer employeeCount = 0;
        if (actualPermission.getForRetail()) {
            employeeCount = permissionRepository.countByForRetailTrue();
        } else if (actualPermission.getForCorporate()) {
            employeeCount = permissionRepository.countByForCorporateTrue();
        } else if (actualPermission.getForTeller()) {
            employeeCount = permissionRepository.countByForTellerTrue();
        } else if (actualPermission.getForPremium()) {
            employeeCount = permissionRepository.countByForPremiumTrue();
        }
        model.addAttribute("EmployeeCount", employeeCount);
        return nextQueueNumber;
    }


    public void setActualPermission(Model model, Employee employee) {
        Permission permission = permissionService.getPermissionByEmployee(employee);
        String actualPermission = null;
        if (permission.getForRetail()) {
            actualPermission = "Lakosság";
        } else if (permission.getForCorporate()) {
             actualPermission = "Vállalat";
        } else if (permission.getForTeller()) {
             actualPermission  = "Pénztár";
        } else if (permission.getForPremium()) {
             actualPermission = "Prémium";
        }
        model.addAttribute("actualPermission", actualPermission);
    }
}







