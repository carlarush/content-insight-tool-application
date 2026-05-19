package com.carla.contentinsighttool.repository;

import com.carla.contentinsighttool.entity.ThemeEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ThemeEntryRepository extends JpaRepository<ThemeEntry, Long> {

    @Query("SELECT t.themeName, SUM(t.count) as total FROM ThemeEntry t " +
           "WHERE t.detectedAt >= :since GROUP BY t.themeName ORDER BY total DESC")
    List<Object[]> findTrendingThemes(@Param("since") LocalDateTime since);
}