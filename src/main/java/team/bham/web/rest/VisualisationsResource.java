package team.bham.web.rest;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.bham.service.VisualisationsService;

@RestController
@RequestMapping("/api")
public class VisualisationsResource {

    private final VisualisationsService visualisationsService;

    public VisualisationsResource(VisualisationsService visualisationsService) {
        this.visualisationsService = visualisationsService;
    }

    @GetMapping("/visualisations")
    public ResponseEntity<JSONObject> getVisualisations(Authentication authentication) {
        JSONObject response = visualisationsService.getShortTermArtists(authentication);
        return ResponseEntity.ok().body(response);
    }
}
