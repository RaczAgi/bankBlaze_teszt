package hu.bankblaze.bankblaze_teszt.repo;

import hu.bankblaze.bankblaze_teszt.model.QueueNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueueNumberRepository extends JpaRepository<QueueNumber, Long> {

    QueueNumber findFirstByOrderByIdAsc();
}