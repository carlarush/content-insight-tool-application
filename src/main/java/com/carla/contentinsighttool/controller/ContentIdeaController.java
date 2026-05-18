package com.carla.contentinsighttool.controller;

import com.carla.contentinsighttool.model.ContentRequest;
import com.carla.contentinsighttool.model.ContentResponse;
import com.carla.contentinsighttool.service.ContentIdeaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/content")
public class ContentIdeaController {

    private final ContentIdeaService service;

    public ContentIdeaController(ContentIdeaService service) {
        this.service = service;
    }

    @PostMapping("/generate")
    public ContentResponse generate(@RequestBody ContentRequest request) {
        return service.generateIdeas(request.getCheckins());
    }
}