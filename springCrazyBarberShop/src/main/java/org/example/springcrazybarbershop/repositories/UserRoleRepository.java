package org.example.springcrazybarbershop.repositories;

import org.example.springcrazybarbershop.models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM UserRole ur WHERE ur.user.id = :userId")
    void deleteAllByUserId(Long userId);
} 