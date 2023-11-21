package hu.bankblaze.bankblaze_teszt.service;

import hu.bankblaze.bankblaze_teszt.model.Premium;
import hu.bankblaze.bankblaze_teszt.repo.PremiumRepository;
import hu.bankblaze.bankblaze_teszt.repo.QueueNumberRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
@NoArgsConstructor
public class PremiumService {

    @Autowired
    private QueueNumberRepository queueNumberRepository;
    private PremiumRepository premiumRepository;

    public List<Premium> getAllPremium() {
        return premiumRepository.findAll();
    }

    public int generateQueueNumber() {
        int queueNumber = queueNumberRepository.getLastNumber(90);
        if (queueNumber < 9000 || queueNumber == 10000) {
            queueNumber = 9000;
        }
        return ++queueNumber;
    }

}