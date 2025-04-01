package org.shounak.sit727chatapp.repository;

import org.shounak.sit727chatapp.model.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DataRepository extends JpaRepository<Data, Long> {
    Optional<Data> findByUsernameAndPassword(String username, String password);

    @Query("select d from Data d where d.username = ?1")
    Optional<Data> findByUsername(String username);

    @Query("select d from Data d where d.username like concat('%', ?1, '%')")
    List<Data> findByUsernameContaining(String username);

    @Query("select d.password from Data d where d.username = ?1")
    Optional<String> findDataPasswordByUsername(String username);
}
