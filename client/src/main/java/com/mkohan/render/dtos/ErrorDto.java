package com.mkohan.render.dtos;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ErrorDto implements Serializable {

    private String message;

    private Date date;
}
