package com.mkohan.render.dtos;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class AuthenticationRequest implements Serializable {

    @NotBlank(message = "Username can't be empty")
    @Length(max = 255, message = "Username max length is 255 characters")
    private String username;

    @NotBlank(message = "Password can't be empty")
    @Length(max = 255, message = "Password max length is 255 characters")
    private String password;
}
