package com.system.demo.bulkprogress.itemdata;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Zeeshan Damani
 */

@Repository
public interface FailItemRepository extends JpaRepository<FailItems, Long> {

    Page<FailItems> findByUserJobId(long UserJobId, Pageable pageable);
    List<FailItems>  findAllByUserJobId(long jobId);
}
