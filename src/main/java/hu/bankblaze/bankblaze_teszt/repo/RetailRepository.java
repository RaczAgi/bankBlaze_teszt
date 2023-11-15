package hu.bankblaze.bankblaze_teszt.repo;

import hu.bankblaze.bankblaze_teszt.model.Retail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RetailRepository extends JpaRepository<Retail, Long> {

}
