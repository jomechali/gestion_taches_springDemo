package com.diginamic.gt.repository;

import com.diginamic.gt.entities.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
/**
 * Manipoulation tache dans la  bse de dpnnées
 */
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Integer> {
    /**
     * SELECT * from confirmation_token ct join empployee e on  e.id = ct.id where ct.value = value and e.username = :username
     * @param value
     * @param username
     * @return
     */
    Optional<ConfirmationToken> findByNumberAndEmployeeUserName(String number, String username);
    Optional<ConfirmationToken> findByNumberAndEmployeeId(String number, int id);

    Optional<ConfirmationToken> findByNumberAndEmployeeUserNameAndActivationIsNull(String number, String username);
}