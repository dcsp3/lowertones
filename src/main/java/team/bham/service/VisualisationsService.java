package team.bham.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import team.bham.domain.AppUser;
import team.bham.domain.User;
import team.bham.repository.AppUserRepository;
import team.bham.repository.UserRepository;
import team.bham.service.APIWrapper.Enums.SpotifyTimeRange;
import team.bham.service.APIWrapper.SpotifyAPIResponse;
import team.bham.service.SpotifyAPIWrapperService;
import team.bham.service.dto.VisualisationsDTO;

@Service
public class VisualisationsService {

    private SpotifyAPIWrapperService spotifyAPIWrapperService;
    private final UserRepository userRepository;
    private final AppUserRepository appUserRepository;

    public VisualisationsService(
        SpotifyAPIWrapperService spotifyAPIWrapperService,
        UserRepository userRepository,
        AppUserRepository appUserRepository
    ) {
        this.spotifyAPIWrapperService = spotifyAPIWrapperService;
        this.userRepository = userRepository;
        this.appUserRepository = appUserRepository;
    }

    public VisualisationsDTO getShortTermArtists(Authentication authentication) {
        VisualisationsDTO dto = new VisualisationsDTO();

        User user = userRepository.findOneByLogin(authentication.getName()).get();
        AppUser appUser = appUserRepository.findByUserId(user.getId()).get();

        JSONArray topArtists = spotifyAPIWrapperService
            .getCurrentUserTopArtists(appUser, SpotifyTimeRange.SHORT_TERM)
            .getData()
            .getJSONArray("items");

        int count = Math.min(5, topArtists.length());

        for (int i = 0; i < count; i++) {
            JSONObject artist = topArtists.getJSONObject(i);
            String name = artist.getString("name");

            switch (i) {
                case 0:
                    dto.setTopArtist1Name(name);
                    break;
                case 1:
                    dto.setTopArtist2Name(name);
                    break;
                case 2:
                    dto.setTopArtist3Name(name);
                    break;
                case 3:
                    dto.setTopArtist4Name(name);
                    break;
                case 4:
                    dto.setTopArtist5Name(name);
                    break;
                default:
                    break;
            }
        }

        return dto;
    }
}
