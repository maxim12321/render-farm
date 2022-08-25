package com.mkohan.render.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "submitter_id", referencedColumnName = "id")
    private User submitter;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "submitted_at", nullable = false, updatable = false)
    private Date submittedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "completed_at")
    private Date completedAt;

    protected Task() {
    }

    public Task(User submitter) {
        this.submitter = submitter;
    }
}
