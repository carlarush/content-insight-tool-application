package com.carla.contentinsighttool.controller;

import com.carla.contentinsighttool.dto.TrendingThemeDto;
import com.carla.contentinsighttool.repository.ThemeEntryRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeEntryRepository themeEntryRepository;

    public ThemeController(ThemeEntryRepository themeEntryRepository) {
        this.themeEntryRepository = themeEntryRepository;
    }

    @GetMapping("/trending")
    public List<TrendingThemeDto> trending() {
        LocalDateTime since = LocalDateTime.now().minusWeeks(4);
        return themeEntryRepository.findTrendingThemes(since).stream()
                .map(row -> new TrendingThemeDto((String) row[0], ((Number) row[1]).longValue()))
                .collect(Collectors.toList());
    }
}