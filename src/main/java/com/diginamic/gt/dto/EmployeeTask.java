package com.diginamic.gt.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeTask {
    @NotNull
    private int idEmployee;
    @NotNull
    private int idTask;
}
