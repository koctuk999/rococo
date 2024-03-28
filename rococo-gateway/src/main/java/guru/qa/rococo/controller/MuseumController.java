package guru.qa.rococo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.rococo.service.MuseumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/museum")
public class MuseumController {
    private final MuseumService museumService;

    @Autowired
    public MuseumController(MuseumService museumService) {
        this.museumService = museumService;
    }

    @GetMapping
    public Page<JsonNode> getAllMuseum(
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) String title
    ) {
        return museumService.getAllMuseum(pageable, title);
    }

    @GetMapping("/{id}")
    public JsonNode getMuseumById(@PathVariable String id) {
        return museumService.getMuseumById(id);
    }

    @PostMapping
    public JsonNode addMuseum(@RequestBody JsonNode request) {
        return museumService.addMuseum(request);
    }

    @PatchMapping
    public JsonNode updateMuseum(@RequestBody JsonNode request) {
        return museumService.updateMuseum(request);
    }
}
