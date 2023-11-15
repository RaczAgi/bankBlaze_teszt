package hu.bankblaze.bankblaze_teszt.service;

import hu.bankblaze.bankblaze_teszt.model.Corporate;
import hu.bankblaze.bankblaze_teszt.repo.CorporateRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class CorporateService {
    @Autowired
    private CorporateRepository corporateRepository;

    public List<Corporate> getAllCorporates() {
        return corporateRepository.findAll();
    }

    public Corporate getCorporateById(Long id) {
        return corporateRepository.findById(id).orElse(null);
    }
}
