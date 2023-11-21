package hu.bankblaze.bankblaze_teszt.repo;

import hu.bankblaze.bankblaze_teszt.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long> {

Permission findByEmployeeId(Long loggedInUserId);

    int countByForRetailTrue();
    int countByForCorporateTrue();
    int countByForTellerTrue();
    int countByForPremiumTrue();

}