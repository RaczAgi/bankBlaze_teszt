package hu.bankblaze.bankblaze_teszt.repo;

import hu.bankblaze.bankblaze_teszt.model.Desk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeskRepository extends JpaRepository<Desk, Long> {
    Desk findByEmployeeId(Long loggedInUserId);
    @Query("SELECT d FROM Desk d WHERE d.employee IS NOT NULL")
    List<Desk> findAllWithEmployeeNotNull();



}

