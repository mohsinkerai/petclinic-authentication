package com.system.demo.bulkprogress.itemdata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Zeeshan Damani
 */

@Entity
@Table(name = "job_failure_items")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
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
