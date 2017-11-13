package com.system.demo.bulkprogress.itemdata;

import lombok.Builder;

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
@Table(name = "job_failure_items")
@Builder
public class FailItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "user_job_id")
    Long userJobId;

    @Column(name = "failed_item")
    String failedItems;

    @Column(name = "failure_reason")
    String failureReason;

}
