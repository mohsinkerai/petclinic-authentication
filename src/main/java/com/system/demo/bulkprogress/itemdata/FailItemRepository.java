package com.system.demo.bulkprogress.itemdata;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Zeeshan Damani
 */

@Repository
public interface FailItemRepository extends JpaRepository<FailItems, Long> {
}
