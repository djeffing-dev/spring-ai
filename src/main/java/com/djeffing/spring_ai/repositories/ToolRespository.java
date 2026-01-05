package com.djeffing.spring_ai.repositories;


import com.djeffing.spring_ai.models.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ToolRespository extends JpaRepository<Tool, Long> {
Optional<Tool> findByName(String name);
@Query(value = "select * from tools where user_id= :userId",nativeQuery = true)
Optional<Tool> findByUserId(@Param("userId") long userId);

    @Query(value = "SELECT * FROM tools WHERE user_id = :userId AND name = :name", nativeQuery = true)
    Optional<Tool> findByUserIdAndName(@Param("userId") long userId, @Param("name") String name);
}

