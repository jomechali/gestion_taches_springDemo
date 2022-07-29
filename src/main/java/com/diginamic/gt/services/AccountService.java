package com.diginamic.gt.services;

import com.diginamic.gt.dto.AuthenticationDTO;
import com.diginamic.gt.dto.TokenDTO;
import com.diginamic.gt.entities.Employee;
import com.diginamic.gt.exceptions.BadRequestException;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends UserDetailsService {

    /**
     * create the account and begin the activation process, ie the confirmationToken
     * @param employee with the first data to create the accont
     * @return
     * @throws BadRequestException
     */
    String signup(Employee employee) throws BadRequestException;

    /**
     * is used to activate an account with the token validation number
     * @param userName
     * @param validationNumber
     * @throws BadRequestException
     */
    void activate(String userName, String validationNumber) throws BadRequestException;

    TokenDTO generateTokens(AuthenticationDTO authenticationDTO);
}
