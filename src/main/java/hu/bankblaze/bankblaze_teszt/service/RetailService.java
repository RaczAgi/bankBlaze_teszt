package hu.bankblaze.bankblaze_teszt.service;

import hu.bankblaze.bankblaze_teszt.model.Retail;
import hu.bankblaze.bankblaze_teszt.repo.RetailRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RetailService {

    @Autowired
    private RetailRepository retailRepository;

    public List<Retail> getAllRetail () {
        return retailRepository.findAll();
    }

    public Retail getRetailById(Long id) {
        return retailRepository.findById(id).orElse(null);
    }
}
