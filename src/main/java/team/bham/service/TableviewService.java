package team.bham.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import team.bham.domain.AppUser;
import team.bham.service.APIWrapper.Enums.SpotifyTimeRange;
import team.bham.service.APIWrapper.SpotifyAPIResponse;

@Service
public class TableviewService {

    private final SpotifyAPIWrapperService apiWrapper;
    private final UserService userService;

    public TableviewService(SpotifyAPIWrapperService apiWrapper, UserService userService) {
        this.apiWrapper = apiWrapper;
        this.userService = userService;
    }
}
