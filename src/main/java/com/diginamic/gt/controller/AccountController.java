package com.diginamic.gt.controller;

import com.diginamic.gt.dto.AuthenticationDTO;
import com.diginamic.gt.dto.TokenDTO;
import com.diginamic.gt.dto.ValidateAccount;
import com.diginamic.gt.entities.Employee;
import com.diginamic.gt.exceptions.BadRequestException;
import com.diginamic.gt.services.AccountService;
import com.diginamic.gt.services.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
public class AccountController {
    private AccountService accountService;
    private EmailService emailService;
    private AuthenticationManager authenticationManager; // provided by spring

    @PostMapping(path = "signin")
    public @ResponseBody TokenDTO signin(@RequestBody AuthenticationDTO authenticationDTO) throws BadRequestException {
        // auth with spring security
        // this will throws an error if failed
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationDTO.getUserName(), authenticationDTO.getPassword()));

        // generate token
        return accountService.generateTokens(authenticationDTO);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "signup")
    public String signup(@RequestBody Employee employee) throws BadRequestException {
        emailService.sendSimpleMessage("jmechali@diginamic-formation.fr", "TEST ENVOI AUTO", "YEAH!!!!!");
        return accountService.signup(employee);
    }

    @PostMapping(path = "activate")
    public void activateAccount(@Validated @RequestBody ValidateAccount validateAccount) throws BadRequestException {
        accountService.activate(validateAccount.getUserName(), validateAccount.getTokenNumber());
    }

}
