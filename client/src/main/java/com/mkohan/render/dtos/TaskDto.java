package com.mkohan.render.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class TaskDto {

    private Long id;
    private Date submittedAt;
    private Date completedAt;
    private Status status;

    private enum Status {
        RENDERING,
        COMPLETE
    }
}
