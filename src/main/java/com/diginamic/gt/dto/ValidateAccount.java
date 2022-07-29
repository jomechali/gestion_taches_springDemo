package com.diginamic.gt.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ValidateAccount {
    @NotNull
    private String userName;
    @NotNull
    private String tokenNumber;

}
