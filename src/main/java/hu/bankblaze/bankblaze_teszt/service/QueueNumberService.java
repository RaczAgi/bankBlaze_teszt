package hu.bankblaze.bankblaze_teszt.service;

import hu.bankblaze.bankblaze_teszt.model.QueueNumber;
import hu.bankblaze.bankblaze_teszt.repo.QueueNumberRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class QueueNumberService {

    @Autowired
    private QueueNumberRepository queueNumberRepository;



    public void deleteQueueNumberById(Long id) {
        queueNumberRepository.deleteById(id);
    }

    public void deleteAllQueueNumbers() {
        queueNumberRepository.deleteAll();
    }

    public QueueNumber getQueueNumber() {
        return queueNumberRepository.findFirstByOrderByIdAsc();
    }

    public QueueNumber getQueueNumberById(Long id) {
        return queueNumberRepository.findById(id).orElse(null);
    }

    public List<QueueNumber> getAllQueueNumbers() {
        return queueNumberRepository.findAll();
    }

    public void generateQueueNumber(QueueNumber newQueueNumber) {
        queueNumberRepository.save(newQueueNumber);
    }
}

