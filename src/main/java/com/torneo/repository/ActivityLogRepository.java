package com.torneo.repository;

import com.torneo.model.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findByUserId(String userId);
    List<ActivityLog> findByDayNumber(int dayNumber);
    List<ActivityLog> findByType(String type);
    List<ActivityLog> findBySubgroupAndType(int subgroup, String type);
}