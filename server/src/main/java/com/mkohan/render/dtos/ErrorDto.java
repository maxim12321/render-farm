package com.mkohan.render.dtos;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ErrorDto implements Serializable {

    private String message;

    private Date date;

    protected ErrorDto() {
    }

    public ErrorDto(String message) {
        this.message = message;
        this.date = new Date();
    }
}
