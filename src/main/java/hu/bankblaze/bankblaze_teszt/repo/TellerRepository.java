package hu.bankblaze.bankblaze_teszt.repo;

import hu.bankblaze.bankblaze_teszt.model.Teller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TellerRepository extends JpaRepository<Teller, Long> {
}
