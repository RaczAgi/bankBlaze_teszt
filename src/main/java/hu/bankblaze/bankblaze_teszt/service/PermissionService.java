package hu.bankblaze.bankblaze_teszt.service;


import hu.bankblaze.bankblaze_teszt.model.Employee;
import hu.bankblaze.bankblaze_teszt.model.Permission;
import hu.bankblaze.bankblaze_teszt.repo.PermissionRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;



    public void savePermisson (Permission permission){
        permissionRepository.save(permission);
    }

    public List<Permission> getAllPermissions(){
        return permissionRepository.findAll();
    }

    private Permission getPermissionById(Long id) {
        return permissionRepository.findById(id).orElse(null);
    }

    public void modifyForRetail(Long id, Boolean newForRetail) {
        Permission permission = getPermissionById(id);
        permission.setForRetail(newForRetail);
        permissionRepository.save(permission);
    }

    public void modifyForCorporate(Long id, Boolean newForCorporate) {
        Permission permission = getPermissionById(id);
        permission.setForCorporate(newForCorporate);
        permissionRepository.save(permission);
    }

    public void modifyForTellers(Long id, Boolean newForTeller) {
        Permission permission = getPermissionById(id);
        permission.setForTeller(newForTeller);
        permissionRepository.save(permission);
    }

    public void modifyForPremium(Long id, Boolean newForPremium) {
        Permission permission = getPermissionById(id);
        permission.setForPremium(newForPremium);
        permissionRepository.save(permission);
    }

    public void updatePermissions(Map<String, String> formData) {
        formData.forEach((key, value) -> {
            if (key.startsWith("for")) {
                String[] parts = key.split("-");
                if (parts.length == 2) {
                    Long id = Long.parseLong(parts[1]);
                    Boolean checked = "true".equals(value);
                    switch (parts[0]) {
                        case "forRetail":
                            modifyForRetail(id, checked);
                            break;
                        case "forCorporate":
                            modifyForCorporate(id, checked);
                            break;
                        case "forTeller":
                            modifyForTellers(id, checked);
                            break;
                        case "forPremium":
                            modifyForPremium(id, checked);
                            break;
                        default:

                            break;
                    }
                }
            }
        });
    }

public Permission getPermissionByEmployee(Employee employee){
       return permissionRepository.findByEmployeeId(employee.getId());
}
    public String getPermissionByLoggedInUser(Long loggedInUserId) {
        Permission permission = permissionRepository.findByEmployeeId(loggedInUserId);
        if (permission != null) {
            if (Boolean.TRUE.equals(permission.getForCorporate())) {
               return "Vállalat";
            } else if (Boolean.TRUE.equals(permission.getForRetail())) {
                return "Lakosság";
            } else if (Boolean.TRUE.equals(permission.getForTeller())) {
                return  "Pénztár";
            } else if (Boolean.TRUE.equals(permission.getForPremium())) {
                return "Prémium";
            } else {
               return "Nincsenek engedélyek";
            }
        } else {
            return "Nincs ilyen azonosítójú felhasználó";
        }

}


    public Permission getPermissions(Long loggedInUserId) {
        return permissionRepository.findByEmployeeId(loggedInUserId);
    }

}
