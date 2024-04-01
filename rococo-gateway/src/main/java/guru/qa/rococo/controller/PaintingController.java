package guru.qa.rococo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.rococo.service.PaintingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/painting")
public class PaintingController {
    private final PaintingService paintingService;

    @Autowired
    public PaintingController(PaintingService paintingService) {
        this.paintingService = paintingService;
    }

    @GetMapping
    public Page<JsonNode> getAllPainting(
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) String title
    ) {
        return paintingService.getAllPainting(pageable, title);
    }

    @GetMapping("/author/{artist}")
    public Page<JsonNode> getPaintingByArtist(@PageableDefault Pageable pageable, @PathVariable String artist) {
        return paintingService.getPaintingByArtist(pageable, artist);
    }

    @GetMapping("/{id}")
    public JsonNode getPaintingById(@PathVariable String id) {
        return paintingService.getPaintingById(id);
    }

    @PostMapping
    public JsonNode addPainting(@RequestBody JsonNode request) {
        return paintingService.addPainting(request);
    }

    @PatchMapping
    public JsonNode updatePainting(@RequestBody JsonNode request) {
        return paintingService.updatePainting(request);
    }
}
