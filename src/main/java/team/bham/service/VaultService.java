package team.bham.service;

import java.net.http.HttpHeaders;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import javax.transaction.Transactional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import team.bham.domain.AppUser;
import team.bham.domain.Vault;
import team.bham.repository.VaultRepository;
import team.bham.service.APIWrapper.SpotifyAPIResponse;
import team.bham.service.APIWrapper.SpotifyPlaylist;
import team.bham.service.APIWrapper.SpotifyTrack;
import team.bham.service.dto.VaultRequest;

@Service
public class VaultService {

    private final SpotifyAPIWrapperService apiWrapper;
    private final UserService userService;

    @Autowired
    private VaultRepository vaultRepository;

    @Autowired
    private TaskScheduler taskScheduler;

    private Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    public VaultService(SpotifyAPIWrapperService apiWrapper, UserService userService, UtilService utilService) {
        this.apiWrapper = apiWrapper;
        this.userService = userService;
    }

    private void scheduleOrUpdateTask(AppUser appUser, Vault vault) {
        ScheduledFuture<?> existingTask = scheduledTasks.get(vault.getId());
        if (existingTask != null && !existingTask.isCancelled()) {
            existingTask.cancel(false);
        }
        Runnable task = () -> updatePlaylist(appUser, vault);
        CronTrigger cronTrigger = new CronTrigger(convertFrequencyToCron(vault.getFrequency()));
        ScheduledFuture<?> future = taskScheduler.schedule(task, cronTrigger);
        scheduledTasks.put(vault.getId(), future);
    }

    private String convertFrequencyToCron(String frequency) {
        switch (frequency) {
            case "Daily":
                return "0 0 0 * * *"; // every day at midnight
            case "Weekly":
                return "0 0 0 * * 0"; // every week on Sunday midnight
            case "Monthly":
                return "0 0 0 1 * *"; // the first day of every month at midnight
            default:
                throw new IllegalArgumentException("Unsupported frequency: " + frequency);
        }
    }

    @Transactional
    public ResponseEntity<String> createPlaylist(Authentication authentication, String playlistName) {
        AppUser appUser = userService.resolveAppUser(authentication.getName());
        if (appUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        SpotifyAPIResponse<JSONObject> response = apiWrapper.createPlaylist(appUser, playlistName);

        return ResponseEntity.ok(response.getData().toString());
    }

    @Transactional
    public ResponseEntity<List<Vault>> getVaultsByUserId(Authentication authentication) {
        AppUser appUser = userService.resolveAppUser(authentication.getName());
        if (appUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<Vault> vaults = vaultRepository.findByUserId(appUser.getId());
        return ResponseEntity.ok(vaults);
    }

    @Transactional
    public void deleteVault(Long id) {
        ScheduledFuture<?> scheduledTask = scheduledTasks.get(id);
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
            scheduledTasks.remove(id);
        }
        vaultRepository.deleteById(id);
    }

    public ResponseEntity<String> addItemsToPlaylist(Authentication authentication, String playlistId, ArrayList<String> playlistIds) {
        AppUser appUser = userService.resolveAppUser(authentication.getName());
        if (appUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        SpotifyAPIResponse<JSONObject> response = apiWrapper.addItemsToPlaylist(appUser, playlistId, playlistIds);

        return ResponseEntity.ok(response.getData().toString());
    }

    public ResponseEntity<SpotifyPlaylist> getPlaylistDetails(Authentication authentication, String playlistId) {
        AppUser appUser = userService.resolveAppUser(authentication.getName());
        if (appUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        SpotifyAPIResponse<SpotifyPlaylist> response = apiWrapper.getPlaylistDetails(appUser, playlistId);

        return ResponseEntity.ok(response.getData());
    }

    private void updatePlaylist(AppUser appUser, Vault vault) {
        SpotifyAPIResponse<SpotifyPlaylist> trackResponse = apiWrapper.getPlaylistDetails(appUser, vault.getSourcePlaylistID());
        if (!trackResponse.getSuccess()) {
            return;
        }

        ArrayList<String> trackIds = extractTrackIds(trackResponse.getData());

        if (!trackIds.isEmpty()) {
            SpotifyAPIResponse<JSONObject> addItemsResponse = apiWrapper.addItemsToPlaylist(appUser, vault.getResultPlaylistID(), trackIds);
            if (!addItemsResponse.getSuccess()) {} else {
                vault.setDateLastUpdated(LocalDate.now());
            }
        }
    }

    private ArrayList<String> extractTrackIds(SpotifyPlaylist playlist) {
        ArrayList<String> trackIds = new ArrayList<>();

        ArrayList<SpotifyTrack> tracks = playlist.getTracks();

        for (SpotifyTrack track : tracks) {
            String trackId = track.getId();
            trackIds.add(trackId);
        }
        return trackIds;
    }

    @Transactional
    public Vault createVaultTask(VaultRequest request, Authentication authentication) throws UsernameNotFoundException {
        AppUser appUser = userService.resolveAppUser(authentication.getName());
        if (appUser == null) {
            throw new UsernameNotFoundException("User not found");
        }

        SpotifyAPIResponse<JSONObject> createResponse = apiWrapper.createPlaylist(appUser, request.getPlaylistName());
        if (!createResponse.getSuccess()) {
            throw new RuntimeException("Failed to create playlist");
        }

        String newPlaylistId = createResponse.getData().getString("id");
        SpotifyAPIResponse<SpotifyPlaylist> trackResponse = apiWrapper.getPlaylistDetails(appUser, request.getSourcePlaylistId());
        if (!trackResponse.getSuccess()) {
            throw new RuntimeException("Failed to fetch tracks from source playlist");
        }

        ArrayList<String> trackIds = extractTrackIds(trackResponse.getData());
        SpotifyAPIResponse<JSONObject> addItemsResponse = apiWrapper.addItemsToPlaylist(appUser, newPlaylistId, trackIds);
        if (!addItemsResponse.getSuccess()) {
            throw new RuntimeException("Failed to add tracks to new playlist");
        }

        Vault vault = new Vault();
        vault.setUserId(appUser.getId());
        vault.setPlaylistName(request.getPlaylistName());
        vault.setSourcePlaylistID(request.getSourcePlaylistId());
        vault.setResultPlaylistID(newPlaylistId);
        vault.setFrequency(request.getFrequency());
        vault.setDateLastUpdated(LocalDate.now());
        vault = vaultRepository.save(vault);

        scheduleOrUpdateTask(appUser, vault);

        return vault;
    }
}
