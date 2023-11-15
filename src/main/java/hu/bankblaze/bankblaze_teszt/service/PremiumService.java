package hu.bankblaze.bankblaze_teszt.service;

import hu.bankblaze.bankblaze_teszt.model.Premium;
import hu.bankblaze.bankblaze_teszt.repo.PremiumRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class PremiumService {

    private PremiumRepository premiumRepository;

    public List<Premium> getAllPremium() {
        return premiumRepository.findAll();
    }

}