package com.torneo.repository;

import com.torneo.model.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    List<Challenge> findByDayNumber(int dayNumber);
}