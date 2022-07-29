package com.diginamic.gt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {

    private String authentication;
    // chaine de securite, envoye par le token expire pour reconnecter l utilisateur
    // ca lie les anciens et nouveaux tokens ici
    // l utilisateur n est pas deco comme ca
    // doit etre implemente par le client
    private String refresh;
}
