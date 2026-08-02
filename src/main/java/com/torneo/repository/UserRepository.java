package com.torneo.repository;

import com.torneo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    
    List<User> findBySubgroup(int subgroup);
    
    @Override
    List<User> findAll();
}
