package team.bham.web.rest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.bham.service.VisualisationsService;
import team.bham.service.dto.VisualisationsDTO;

@RestController
@RequestMapping("/api")
public class VisualisationsResource {

    private final VisualisationsService visualisationsService;

    public VisualisationsResource(VisualisationsService visualisationsService) {
        this.visualisationsService = visualisationsService;
    }

    @PostMapping("/visualisations")
    public ResponseEntity<VisualisationsDTO> getVisualisations(Authentication authentication, @RequestBody String playlistId) {
        VisualisationsDTO response = visualisationsService.getVisualisations(authentication, playlistId);
        return ResponseEntity.ok().body(response);
    }
}
