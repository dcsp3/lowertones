package team.bham.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import team.bham.domain.User;
import team.bham.repository.UserRepository;
import team.bham.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api")
public class APIScrapingResource {

    private final UserRepository userRepository;

    //todo
    public APIScrapingResource(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/scrape")
    public ResponseEntity<Boolean> ScrapeUserPlaylists() {
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("/test-get-user-id")
    public ResponseEntity<Long> getUserID(Authentication authentication) {
        String userName = authentication.getName();
        User user = new User();
        user = userRepository.findOneByLogin(userName).get();
        return new ResponseEntity<>(user.getId(), HttpStatus.OK);
        // return new ResponseEntity<>(authentication.getName(), HttpStatus.OK);
    }
}
