package com.diginamic.gt.services.implementations;

import com.diginamic.gt.entities.ConfirmationToken;
import com.diginamic.gt.entities.Employee;
import com.diginamic.gt.repository.ConfirmationTokenRepository;
import com.diginamic.gt.services.ConfirmationService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@AllArgsConstructor
@Getter
@Setter
public class ConfirmationServiceImpl implements ConfirmationService {

    private ConfirmationTokenRepository confirmationTokenRepository;

    /**
     * @param employee
     * @return
     */
    @Override
    public String sendEmployeeVerificationToken(Employee employee) {
        // generer un token, pas au sens securise, mais au sens numero d activation de compte
        // ou encore pour mettre enabled a true

        String validationToken = RandomStringUtils.random(6, false, true);
        // sauvegader le token

        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setNumber(validationToken);
        confirmationToken.setEmployee(employee);
        confirmationToken.setCreation(Instant.now());

        this.confirmationTokenRepository.save(confirmationToken);
        // envoie a l utilisateur par mail ou sms
        return validationToken;

    }

    /**
     * @param number
     * @param username
     * @return
     */
    @Override
    public Optional<ConfirmationToken> findByNumberAndEmployeeUserName(String number, String username) {
        return confirmationTokenRepository.findByNumberAndEmployeeUserName(number, username);
    }

    /**
     * @param number
     * @param id
     * @return
     */
    @Override
    public Optional<ConfirmationToken> findByNumberAndEmployeeId(String number, int id) {
        return confirmationTokenRepository.findByNumberAndEmployeeId(number, id);
    }

    @Override
    public Optional<ConfirmationToken> findByNumberAndEmployeeUserNameAndActivationIsNull(String number, String username) {
        return confirmationTokenRepository.findByNumberAndEmployeeUserNameAndActivationIsNull(number, username);
    }
}
