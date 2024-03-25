package team.bham.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.bham.service.RecappedService;
import team.bham.service.dto.RecappedDTO;
import team.bham.service.dto.RecappedRequest;

@RestController
@RequestMapping("/recapped")
public class RecappedResource {

    private final RecappedService recappedService;

    public RecappedResource(RecappedService recappedService) {
        this.recappedService = recappedService;
    }

    // POST method to accept criteria and return the recapped information
    @PostMapping("/recapped")
    public ResponseEntity<RecappedDTO> getRecappedInfo(@RequestBody RecappedRequest request, Authentication authentication) {
        // Here, you would use the request parameters to perform your business logic
        // For simplicity, I'm directly calling a hypothetical method in the service
        RecappedDTO result = recappedService.calculateRecappedInfo(request, authentication);
        return ResponseEntity.ok(result);
    }
}
