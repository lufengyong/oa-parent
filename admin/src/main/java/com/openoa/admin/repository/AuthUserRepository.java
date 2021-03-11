package com.openoa.admin.repository;

import com.openoa.admin.entity.AuthUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUserDetails,Integer> {
}
