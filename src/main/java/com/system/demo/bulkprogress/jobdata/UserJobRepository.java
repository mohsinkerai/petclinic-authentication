package com.system.demo.bulkprogress.jobdata;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserJobRepository extends JpaRepository<UserJobData, Long> {

    @Query(value = "from UserJobData where jobId = :jobId")
    public UserJobData getUserJobDataByJobId(@Param("jobId") Long jobId);

    Page<UserJobData> findByUserId(long UserId, Pageable pageable);
}
