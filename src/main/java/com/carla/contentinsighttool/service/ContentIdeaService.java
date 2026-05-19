package com.carla.contentinsighttool.service;

import com.carla.contentinsighttool.entity.CheckinBatch;
import com.carla.contentinsighttool.entity.ContentIdea;
import com.carla.contentinsighttool.entity.ThemeEntry;
import com.carla.contentinsighttool.model.ContentResponse;
import com.carla.contentinsighttool.model.ThemeResult;
import com.carla.contentinsighttool.repository.CheckinBatchRepository;
import com.carla.contentinsighttool.repository.ContentIdeaRepository;
import com.carla.contentinsighttool.repository.ThemeEntryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContentIdeaService {

    private final OpenAiIdeaService openAiIdeaService;
    private final CheckinBatchRepository checkinBatchRepository;
    private final ThemeEntryRepository themeEntryRepository;
    private final ContentIdeaRepository contentIdeaRepository;

    public ContentIdeaService(OpenAiIdeaService openAiIdeaService,
                              CheckinBatchRepository checkinBatchRepository,
                              ThemeEntryRepository themeEntryRepository,
                              ContentIdeaRepository contentIdeaRepository) {
        this.openAiIdeaService = openAiIdeaService;
        this.checkinBatchRepository = checkinBatchRepository;
        this.themeEntryRepository = themeEntryRepository;
        this.contentIdeaRepository = contentIdeaRepository;
    }

    public ContentResponse generateIdeas(List<String> checkins) {
        Map<String, Integer> themeCounts = new LinkedHashMap<>();

        for (String checkin : checkins) {
            String text = checkin.toLowerCase();

            if (text.contains("weekend")
                    || text.contains("friday")
                    || text.contains("saturday")
                    || text.contains("sunday")
                    || text.contains("lost control at the weekend")
                    || text.contains("weekends are hard")) {
                increment(themeCounts, "weekend overeating");
            }

            if (text.contains("guilt")
                    || text.contains("guilty")
                    || text.contains("ruined it")
                    || text.contains("bad with food")
                    || text.contains("ashamed")
                    || text.contains("i messed up")) {
                increment(themeCounts, "food guilt");
            }

            if (text.contains("start again monday")
                    || text.contains("make up for it")
                    || text.contains("compensate")
                    || text.contains("skip meals")
                    || text.contains("be good tomorrow")
                    || text.contains("off track")
                    || text.contains("on track")) {
                increment(themeCounts, "all-or-nothing thinking");
            }

            if (text.contains("feel fat")
                    || text.contains("hate my body")
                    || text.contains("body image")
                    || text.contains("uncomfortable in my body")
                    || text.contains("bloated and awful")
                    || text.contains("felt huge")) {
                increment(themeCounts, "body image struggles");
            }

            if (text.contains("eat in front of people")
                    || text.contains("embarrassed eating")
                    || text.contains("social meal")
                    || text.contains("eating out with others")
                    || text.contains("uncomfortable eating around people")
                    || text.contains("eat around people")
                    || text.contains("eating in front of people")) {
                increment(themeCounts, "social eating anxiety");
            }

            if (text.contains("binge")
                    || text.contains("binged")
                    || text.contains("overeating")
                    || text.contains("overeat")
                    || text.contains("restricted")
                    || text.contains("ate less earlier")
                    || text.contains("out of control with food")
                    || text.contains("lost control around food")) {
                increment(themeCounts, "binge/restrict cycle");
            }

            if (text.contains("emotional eating")
                    || text.contains("stress eating")
                    || text.contains("ate because i was stressed")
                    || text.contains("comfort ate")
                    || text.contains("ate my feelings")
                    || text.contains("eating when anxious")
                    || text.contains("eating when upset")
                    || text.contains("snacking because i was stressed")) {
                increment(themeCounts, "emotional eating");
            }

            if (text.contains("tracking")
                    || text.contains("myfitnesspal")
                    || text.contains("logged everything")
                    || text.contains("didn't track")
                    || text.contains("did not track")
                    || text.contains("scared to stop tracking")
                    || text.contains("need to track")
                    || text.contains("track my food")) {
                increment(themeCounts, "tracking dependence");
            }

            if (text.contains("fear of weight gain")
                    || text.contains("scared of gaining weight")
                    || text.contains("afraid to gain weight")
                    || text.contains("worried about weight gain")
                    || text.contains("weight gain")) {
                increment(themeCounts, "fear of weight gain");
            }

            if (text.contains("perfect")
                    || text.contains("perfectionist")
                    || text.contains("all or nothing")
                    || text.contains("had one bad meal")
                    || text.contains("wanted to do it perfectly")) {
                increment(themeCounts, "perfectionism");
            }
        }

        Map<String, Integer> topThemeCounts = getTopThemeCounts(themeCounts);
        List<ThemeResult> themeResults = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : topThemeCounts.entrySet()) {
            String theme = entry.getKey();
            int count = entry.getValue();

            List<String> contentIdeas = new ArrayList<>();
            List<String> podcastIdeas = new ArrayList<>();
            List<String> resourceIdeas = new ArrayList<>();

            switch (theme) {
                case "weekend overeating":
                    contentIdeas.add("Why trying to 'make up for the weekend' keeps you stuck");
                    contentIdeas.add("Weekend overeating is often a response to restriction, not lack of discipline");
                    podcastIdeas.add("Why weekends feel out of control with food");
                    resourceIdeas.add("Weekend reset reflection sheet");
                    break;

                case "food guilt":
                    contentIdeas.add("Why guilt after eating often leads to more overeating");
                    contentIdeas.add("One overeating episode does not mean you've ruined anything");
                    podcastIdeas.add("Food guilt and the binge cycle");
                    resourceIdeas.add("Food guilt reframe worksheet");
                    break;

                case "all-or-nothing thinking":
                    contentIdeas.add("The real damage caused by 'I'll start again Monday'");
                    contentIdeas.add("You're not off track — you're one decision away");
                    podcastIdeas.add("All-or-nothing thinking explained");
                    resourceIdeas.add("Thought reframe guide");
                    break;

                case "binge/restrict cycle":
                    contentIdeas.add("Why being 'good' all week sets up the binge");
                    contentIdeas.add("This is not a willpower problem");
                    podcastIdeas.add("What keeps the cycle going");
                    resourceIdeas.add("Post-binge reset guide");
                    break;
            }

            themeResults.add(new ThemeResult(
                    theme,
                    count,
                    contentIdeas,
                    podcastIdeas,
                    resourceIdeas
            ));
        }

        String summary = buildSummary(topThemeCounts);
        Map<String, Object> aiIdeas = openAiIdeaService.generateIdeas(topThemeCounts);

        persist(checkins, topThemeCounts, aiIdeas);

        return new ContentResponse(summary, themeResults, aiIdeas);
    }

    private void persist(List<String> checkins, Map<String, Integer> topThemeCounts,
                         Map<String, Object> aiIdeas) {
        LocalDateTime now = LocalDateTime.now();
        CheckinBatch batch = checkinBatchRepository.save(new CheckinBatch(checkins, now));

        for (Map.Entry<String, Integer> entry : topThemeCounts.entrySet()) {
            themeEntryRepository.save(new ThemeEntry(entry.getKey(), entry.getValue(), now, batch));
        }

        if (!aiIdeas.containsKey("error")) {
            saveAiIdeas(aiIdeas, batch, now);
        }
    }

    @SuppressWarnings("unchecked")
    private void saveAiIdeas(Map<String, Object> aiIdeas, CheckinBatch batch, LocalDateTime generatedAt) {
        List<String> contentIdeas = (List<String>) aiIdeas.getOrDefault("contentIdeas", List.of());
        List<String> podcastIdeas = (List<String>) aiIdeas.getOrDefault("podcastIdeas", List.of());
        List<String> resourceIdeas = (List<String>) aiIdeas.getOrDefault("resourceIdeas", List.of());

        for (String idea : contentIdeas) {
            contentIdeaRepository.save(new ContentIdea(idea, "content", generatedAt, batch));
        }
        for (String idea : podcastIdeas) {
            contentIdeaRepository.save(new ContentIdea(idea, "podcast", generatedAt, batch));
        }
        for (String idea : resourceIdeas) {
            contentIdeaRepository.save(new ContentIdea(idea, "resource", generatedAt, batch));
        }
    }

    private void increment(Map<String, Integer> map, String key) {
        map.put(key, map.getOrDefault(key, 0) + 1);
    }

    private Map<String, Integer> getTopThemeCounts(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(map.entrySet());
        entries.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        Map<String, Integer> topThemeCounts = new LinkedHashMap<>();
        int limit = Math.min(3, entries.size());

        for (int i = 0; i < limit; i++) {
            topThemeCounts.put(entries.get(i).getKey(), entries.get(i).getValue());
        }

        return topThemeCounts;
    }

    private String buildSummary(Map<String, Integer> topThemeCounts) {
        if (topThemeCounts.isEmpty()) {
            return "No clear recurring themes were detected in this batch of check-ins.";
        }

        List<String> parts = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : topThemeCounts.entrySet()) {
            parts.add(entry.getKey() + " (" + entry.getValue() + ")");
        }

        return "Most common themes this week: " + String.join(", ", parts) + ".";
    }
}