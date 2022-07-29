package com.diginamic.gt.services.implementations;

import com.diginamic.gt.dto.AuthenticationDTO;
import com.diginamic.gt.dto.TokenDTO;
import com.diginamic.gt.entities.ConfirmationToken;
import com.diginamic.gt.entities.Employee;
import com.diginamic.gt.entities.Role;
import com.diginamic.gt.enums.UserRole;
import com.diginamic.gt.exceptions.BadRequestException;
import com.diginamic.gt.repository.RoleRepository;
import com.diginamic.gt.security.JWTTokenUtils;
import com.diginamic.gt.services.AccountService;
import com.diginamic.gt.services.ConfirmationService;
import com.diginamic.gt.services.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private EmployeeService employeeService;

    private ConfirmationService confirmationService;

    private JWTTokenUtils jWTTokenUtils;

    private RoleRepository roleRepository; // should be a service
    /**
     * @param employee
     * @return
     */
    @Override
    public String signup(Employee employee) throws BadRequestException {
        // check password
        if (employee.getPassword() == null || employee.getPassword().length() < 8){
            throw new BadRequestException("mot de passe mal formatte", "ILL_FORMATTED");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(employee.getPassword());
        log.info("passe en clair {} passe crypte {}", employee.getPassword(), encodedPassword);
        employee.setPassword(encodedPassword);

        // using the DB identifiers let us depend less on code which can change greatly
        Role role = roleRepository.findByName(UserRole.USER).orElse(null);

        if (employee.getRoles() == null) {
            employee.setRoles(new ArrayList<>());
        }

        employee.getRoles().add(role);

        // creer l employe
        employee.setEnabled(false); // devient true a la connexion?
        employeeService.create(employee);

        // temp pour avoir le token de securite
        return confirmationService.sendEmployeeVerificationToken(employee);
    }

    /**
     * permet a spring de trouver les donnees dans la db, pour les infos de securite
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return employeeService.findByUserName(username);
    }

    @Override
    public void activate(String userName, String validationNumber) throws BadRequestException {
        Optional<ConfirmationToken> confirmationTokenOptional = confirmationService.findByNumberAndEmployeeUserNameAndActivationIsNull(validationNumber, userName);

        // cas metier : il faut pouvoir virer les tokens invalides, sinon les utilisateurs ne pourront plus utiliser de se connecter
        //if (token.isEmpty() || Duration.between(token.get().getCreatedAt(), Instant.now()).toMinutes() > 10)
        if (confirmationTokenOptional.isEmpty()){
            throw new BadRequestException("le code est faux", "WRONG_CODE");
        }

        if (Duration.between(confirmationTokenOptional.get().getCreation(), Instant.now()).toMinutes() > 5) {
            throw new BadRequestException("ce token n est plus valide", "EXPIRED_TOKEN");
        }

        if (!confirmationTokenOptional.get().getNumber().equals(validationNumber)) {
            throw new BadRequestException("il n y a pas de token a activer", "NO_TOKEN_FOR_EMPLOYEE");
        }

        confirmationTokenOptional.get().setActivation(Instant.now());
        employeeService.findByUserName(userName).setEnabled(true);
    }

    /**
     * @param authenticationDTO
     * @return
     */
    @Override
    public TokenDTO generateTokens(AuthenticationDTO authenticationDTO) {
        Employee employee = this.employeeService.findByUserName(authenticationDTO.getUserName());
        TokenDTO tokens = new TokenDTO();

        // stoquer un objet authdata (jointure entre le tokendto et l employee) en db
        // authdataservice.addtoken(authentification, securitytoken, employee)
        String authenticationToken = jWTTokenUtils.generateToken(employee);
        tokens.setAuthentication(authenticationToken);
        tokens.setRefresh(RandomStringUtils.random(40, true, true)); // to store somewhere to check when the token will expire
        return tokens;
    }


}
