package it.giocode.cv_managment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@ToString
public class UserPrincipal implements Serializable {
    private final Long id;
    private final String email;
}