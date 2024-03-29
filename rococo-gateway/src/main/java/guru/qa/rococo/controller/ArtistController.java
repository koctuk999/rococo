package guru.qa.rococo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.rococo.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/artist")
public class ArtistController {
    private final ArtistService artistService;

    @Autowired
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping
    public Page<JsonNode> getAllArtists(
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) String name
    ) {
        return artistService.getAllArtists(pageable, name);
    }

    @GetMapping("/{id}")
    public JsonNode getArtistById(@PathVariable String id) {
        return artistService.getArtistById(id);
    }

    @PostMapping
    public JsonNode addArtist(@RequestBody JsonNode request) {
        return artistService.addArtist(request);
    }

    @PatchMapping
    public JsonNode updateArtist(@RequestBody JsonNode request) {
        return artistService.updateArtist(request);
    }
}
