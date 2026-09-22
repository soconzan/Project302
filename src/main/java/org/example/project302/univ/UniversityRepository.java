package org.example.project302.univ;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UniversityRepository extends JpaRepository<University, Long> {

    @Query("SELECT u FROM University u WHERE u.univName LIKE %:univName%")
    List<University> findByUniversityNameContaining(@Param("univName") String univName);
}
