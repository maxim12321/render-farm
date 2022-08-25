package com.mkohan.render.dtos;

import com.mkohan.render.entities.Task;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TaskDto {

    private Long id;
    private Date submittedAt;
    private Date completedAt;
    private Status status;

    protected TaskDto() {
    }

    public TaskDto(Task task) {
        this.id = task.getId();
        this.submittedAt = task.getSubmittedAt();
        this.completedAt = task.getCompletedAt();

        this.status = completedAt == null ? Status.RENDERING : Status.COMPLETE;
    }

    private enum Status {
        RENDERING,
        COMPLETE
    }
}
