package com.diginamic.gt.services;

import com.diginamic.gt.entities.ConfirmationToken;
import com.diginamic.gt.entities.Employee;

import java.util.Optional;

public interface ConfirmationService {
    String sendEmployeeVerificationToken(Employee employee);


    Optional<ConfirmationToken> findByNumberAndEmployeeUserName(String number, String username);
    Optional<ConfirmationToken> findByNumberAndEmployeeId(String number, int id);

    Optional<ConfirmationToken> findByNumberAndEmployeeUserNameAndActivationIsNull(String number, String username);

    // we also could validate here the number
}
