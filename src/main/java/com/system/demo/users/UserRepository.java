package com.system.demo.users;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<MyUser,Long> {

    Optional<MyUser> findByUsername(String username);

//    Page<Vehicle> findByEnabledTrue(Pageable pageable);
}
