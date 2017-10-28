package org.springframework.samples.petclinic.users;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.vehicle.Vehicle;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<MyUser,Long> {

    Optional<MyUser> findByUsername(String username);

//    Page<Vehicle> findByEnabledTrue(Pageable pageable);
}
