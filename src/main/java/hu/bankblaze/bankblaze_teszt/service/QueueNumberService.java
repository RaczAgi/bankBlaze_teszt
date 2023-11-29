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
    private PermissionService permissionService;


    public void deleteQueueNumberById(Long id) {
        queueNumberRepository.deleteById(id);
    }

    public void deleteAllQueueNumbers() {
        queueNumberRepository.deleteAll();
    }

    public QueueNumber getQueueNumber() {
        return queueNumberRepository.findFirstByOrderByIdDesc();
    }

    public QueueNumber getQueueNumberById(Long id) {
        return queueNumberRepository.findById(id).orElse(null);
    }

    public List<QueueNumber> getAllQueueNumbers() {
        return queueNumberRepository.findAll();
    }

    public void addQueueNumber(QueueNumber newQueueNumber) {
        queueNumberRepository.save(newQueueNumber);
    }

    public void modifyName(String newName) {
        QueueNumber queueNumber = getQueueNumber();
        queueNumber.setName(newName);
        queueNumberRepository.save(queueNumber);
    }

    public void modifyNumber(int newNumber) {
        QueueNumber queueNumber = getQueueNumber();
        queueNumber.setNumber(newNumber);
        queueNumberRepository.save(queueNumber);
    }

    public void modifyToRetail(Boolean isToRetail) {
        QueueNumber queueNumber = getQueueNumber();
        queueNumber.setToRetail(isToRetail);
        queueNumberRepository.save(queueNumber);
    }

    public void modifyToCorporate(Boolean isToCorporate) {
        QueueNumber queueNumber = getQueueNumber();
        queueNumber.setToCorporate(isToCorporate);
        queueNumberRepository.save(queueNumber);
    }

    public void modifyToTeller(Boolean isToTeller) {
        QueueNumber queueNumber = getQueueNumber();
        queueNumber.setToTeller(isToTeller);
        queueNumberRepository.save(queueNumber);
    }

    public void modifyToPremium(Boolean isToPremium) {
        QueueNumber queueNumber = getQueueNumber();
        queueNumber.setToPremium(isToPremium);
        queueNumberRepository.save(queueNumber);
    }

    public void deleteQueueNumber() {
        QueueNumber queueNumber = getQueueNumber();
        queueNumberRepository.deleteById(queueNumber.getId());
    }
    public void deleteQueueNumberByNumber(int numberToDelete) {
        queueNumberRepository.deleteByNumber(numberToDelete);
    }



}



