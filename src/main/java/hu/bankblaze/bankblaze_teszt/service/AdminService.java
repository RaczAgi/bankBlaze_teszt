package hu.bankblaze.bankblaze_teszt.service;

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

    public Employee getAdminByName(String name) {
        return employeeRepository.findByName(name).orElse(null);
    }
    public Long getLoggedInUserIdByUsername(String loggedInUsername) {
        Optional<Employee> employeeOptional = employeeRepository.findByName(loggedInUsername);
        return employeeOptional.map(Employee::getId).orElse(null);
    }

    public String getLoggedInClerks(Model model) {
        String loggedInUsername = getLoggedInUsername();
        model.addAttribute("loggedInUsername", loggedInUsername);

        Long loggedInUserId = getLoggedInUserIdByUsername(loggedInUsername);
        Long deskNumber = deskService.getDeskIdByLoggedInUser(loggedInUserId);
        model.addAttribute("deskNumber", deskNumber);

        setQueueCounts(model);
        setActualCount(model);
        setEmployeeCount(model);

        String permission = permissionService.getPermissionByLoggedInUser(loggedInUserId);
        model.addAttribute("permission", permission);

        return "employee";
    }

    public String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public void setQueueCounts(Model model) {
        model.addAttribute("lakossagCount", queueNumberRepository.countByNumberBetweenAndActiveIsTrue(1000, 1999));
        model.addAttribute("vallalatCount", queueNumberRepository.countByNumberBetweenAndActiveIsTrue(2000, 2999));
        model.addAttribute("penztarCount", queueNumberRepository.countByNumberBetweenAndActiveIsTrue(3000, 3999));
        model.addAttribute("premiumCount", queueNumberRepository.countByNumberBetweenAndActiveIsTrue(9000, 9999));
    }

    public void setActualCount(Model model) {
        String permission = (String) model.getAttribute("permission");

        Integer actualCount = 0;
        if ("Lakosság".equals(permission)) {
            actualCount = queueNumberRepository.countByNumberBetweenAndActiveIsTrue(1000, 1999);
        } else if ("Vállalat".equals(permission)) {
            actualCount = queueNumberRepository.countByNumberBetweenAndActiveIsTrue(2000, 2999);
        } else if ("Pénztár".equals(permission)) {
            actualCount = queueNumberRepository.countByNumberBetweenAndActiveIsTrue(3000, 3999);
        } else if ("Prémium".equals(permission)) {
            actualCount = queueNumberRepository.countByNumberBetweenAndActiveIsTrue(9000, 9999);
        }
        model.addAttribute("actualCount", actualCount);
    }

    public void setEmployeeCount(Model model) {
        String permission = (String) model.getAttribute("permission");

        Integer letszamCount = 0;
        if ("Lakosság".equals(permission)) {
            letszamCount = permissionRepository.countByForRetailTrue();
        } else if ("Vállalat".equals(permission)) {
            letszamCount = permissionRepository.countByForCorporateTrue();
        } else if ("Pénztár".equals(permission)) {
            letszamCount = permissionRepository.countByForTellerTrue();
        } else if ("Prémium".equals(permission)) {
            letszamCount = permissionRepository.countByForPremiumTrue();
        }
        model.addAttribute("letszamCount", letszamCount);
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
            nextQueueNumber.setToRetail("lakossag".equals(redirectLocation));
            nextQueueNumber.setToCorporate("vallalat".equals(redirectLocation));
            nextQueueNumber.setToTeller("penztar".equals(redirectLocation));
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
            queueNumberRepository.delete(nextQueueNumber);
        }
    }
    public QueueNumber processNextClient(Model model) {
        String loggedInUsername = getLoggedInUsername();
        Long loggedInUserId = getLoggedInUserIdByUsername(loggedInUsername);
        Long deskNumber = deskService.getDeskIdByLoggedInUser(loggedInUserId);
        String permission = permissionService.getPermissionByLoggedInUser(loggedInUserId);
        Permission permissions = permissionService.getPermissions(loggedInUserId);

        QueueNumber nextQueueNumber = determineNextQueueNumber(permission, permissions);

        prepareModel(model, loggedInUsername, deskNumber, permission, nextQueueNumber);


       Integer actualCount = 0;
        if (permission.equals("Lakosság")) {
            actualCount = queueNumberRepository.countByNumberBetweenAndActiveIsTrue(1000, 1999);
        } else if (permission.equals("Vállalat")) {
            actualCount = queueNumberRepository.countByNumberBetweenAndActiveIsTrue(2000, 2999);
        } else if (permission.equals("Pénztár")) {
            actualCount = queueNumberRepository.countByNumberBetweenAndActiveIsTrue(3000, 3999);
        } else if (permission.equals("Prémium")) {
            actualCount = queueNumberRepository.countByNumberBetweenAndActiveIsTrue(9000, 9999);
        }
        model.addAttribute("actualCount", actualCount);
        Integer employeeCount = 0;
        if (permission.equals("Lakosság")) {
            employeeCount = permissionRepository.countByForRetailTrue();
        } else if (permission.equals("Vállalat")) {
            employeeCount = permissionRepository.countByForCorporateTrue();
        } else if (permission.equals("Pénztár")) {
            employeeCount = permissionRepository.countByForTellerTrue();
        } else if (permission.equals("Prémium")) {
            employeeCount = permissionRepository.countByForPremiumTrue();
        }
        model.addAttribute("EmployeeCount", employeeCount);
        return nextQueueNumber;
    }


}







