package com.system.demo.bulkprogress.jobdata;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Zeeshan Damani
 */
@Entity
@Table(name = "user_job_data")
@Builder
@Getter
@Setter
public class UserJobData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "user_id")
    Long userId;

    @Column(name = "job_id")
    Long jobId;

    @Column(name = "job_name")
    String jobName;

    @Column(name = "job_type")
    String jobType;

    @Column(name = "job_status")
    String jobStatus;
}
