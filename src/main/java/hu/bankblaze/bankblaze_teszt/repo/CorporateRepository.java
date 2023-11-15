package hu.bankblaze.bankblaze_teszt.repo;

import hu.bankblaze.bankblaze_teszt.model.Corporate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorporateRepository extends JpaRepository<Corporate, Long> {
}