//SongRepository.java
package com.torneo.repository;

import com.torneo.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByDayNumber(int dayNumber);
    List<Song> findByDayNumberAndTeam(int dayNumber, String team);
}