package team.bham.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;
import org.checkerframework.checker.units.qual.s;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import team.bham.repository.AlbumRepository;
import team.bham.service.DatabaseImportService;
import team.bham.service.DatabaseImportService;

@Component
public class ApplicationStartupRunner implements ApplicationListener<ApplicationReadyEvent> {

    private static final String SERVICE_ACCOUNT_FILE = "./src/main/resources/config/drive_login.json";

    private static final String[] FILE_IDS = {
        "19WAvky8Gwe_3eo0AGipNRogsFS5ZQhth",
        "1qXXWnNcyfSEFeeJ1How11bY8QiWJDkkT",
        "1K6mjK69grqvMv6J-Wb6ra5tdePEeY_GB",
        "19LoGvu1-qnQndoEowqBGuEm2zgUo_4GV",
        "1UtN9son_AZ3X5q0t9zFtrl3uSXKnV4z-",
        "1QBGgdEtkIcq0znJdJKECHriNgFgckUIO",
        "1hCzb7caSxKGphkJYNeoadhiy7t8XPgKp",
        "1W_ynQoNyHx4C9Q_I_agJN8qFhPj2wgM7",
        "1ocwUOgmoKOIL7_rJmbFF2fNV-4b06E5M",
        "1Q_EYDwO8ieSgyPECk2b8RcaNx9mvPv5w",
        "1mFWir3U1_1DiR1kas3nvzBLQ5tJylzUg",
        "139n9j-XXbvG_-FTf960gQZle5vWBTJa3",
        "1ZUXSQi4MR5MvI4HVcgRfkuwwvN-o730F",
        "1W1FEWH38jkEZVB1J8NVRyI5ZWgvIz3kw",
        "1l7kxHR6tkaBYvtVNGJvuxdAiYzqIH6ks",
        "19hnO5YfbnMYmrPuT21PdnWw6v8zqp9kW",
        "1ViIHJf7JpNWGempHGbvJAyVZc2OgmfAT",
        "1-dYCYpWw5s-WvQSOxzOlRg2wQwfp0VlX",
        "1VwnoVlvJHAiUTV214mukKEZM7sPBrFje",
        "1EKKPyeowsYjxdd06tk4j8h6cR85Q6apj",
        "1mapytHZePrKTUcIzaR22unIPyxSIsKkS",
        "1QtjIAnUTNT6MXn3tUKifri71Hyz3wK1K",
        "15mBrYtrjIzcg55xyK5FAY1Qen2lU3w4B",
        "1tGON-fXMsGXiu4CRUn8r_hP0q-1gpEk2",
        "1fm9LoGTgbMk6AQEIxyQj7NfCPLfY-0lL",
        "1EozBUna8etfubulF4YSFk_-sJWZnkDfc",
        "1XioUm0sv_TX5__dQzpYqoirmD3PoRttn",
        "1KFdqshnwEm7zOqqcJMk6hrwb3bZIC0KR",
        "1F3I9mK-zG6EffUzeraOxpSE9Sfk4Kikt",
        "1ru5Pjv_X_1NONDRwYvk2oR54fAVftCHS",
        "1ElKPRX1lU1BYJlzuBiDTJOxfGWE7BsKA",
        "1qf2-nSTH7QakM8JycAonE-1ELY9kujA0",
        "1pT9KgjYOz7enP77_eZ0GRmKUvORKF2e7",
        "1mlwjB8IcUiCitF2MpISHaWmt724dDin4",
    };

    @Value("${spring.liquibase.contexts}")
    private String activeProfile;

    private final DatabaseImportService databaseImportService;
    private final AlbumRepository albumRepository;

    @Autowired
    public ApplicationStartupRunner(DatabaseImportService databaseImportService, AlbumRepository albumRepository) {
        this.databaseImportService = databaseImportService;
        this.albumRepository = albumRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("Application is ready and live!");
        System.out.println("Current working directory: " + System.getProperty("user.dir"));
        // lol
        if (albumRepository.count() < 10000) runCustomScript();
    }

    private void runCustomScript() {
        System.out.println("Running custom script...");
        if (activeProfile.contains("prod")) {
            // download
            Drive service = null;
            try {
                service = createDriveService(driveLogin);
                System.out.println("1");
            } catch (IOException e) {
                System.out.println("2");
                e.printStackTrace();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
            System.out.println("3");

            String outputDirectory = "./tmp/downloaded_files";
            java.io.File fileOutputDir = new java.io.File(outputDirectory);
            if (!fileOutputDir.exists()) {
                if (fileOutputDir.mkdirs()) {
                    System.out.println("Directory created: " + outputDirectory);
                } else {
                    System.out.println("Failed to create directory: " + outputDirectory);
                    return;
                }
            }

            System.out.println("4");

            for (String fileID : FILE_IDS) {
                String fileName = null;
                System.out.println("5");

                try {
                    fileName = downloadFile(service, fileID, outputDirectory);
                    System.out.println("6");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("File " + fileName + " has been downloaded and saved in " + outputDirectory);
                System.out.println("6");
            }
            System.out.println("7");

            runImport();
        } else if (activeProfile.contains("dev")) {
            try {
                // runImport();
            } catch (Exception e) {}
        } else {
            System.out.println("wtf");
        }
    }

    private void runImport() {
        // run import
        try {
            System.out.println("8");
            System.out.println("Importing artist data...");
            Map<Long, Long> artistIDMap = databaseImportService.importArtists("/tmp/downloaded_files/artist_data.csv");
            System.out.println("artist data imported successfully!");

            System.out.println("Importing album data part 1...");
            Map<Long, Long> albumIDMap = databaseImportService.importAlbums("/tmp/downloaded_files/album_full.csv");
            // System.out.println("Importing album data part 2...");
            // albumIDMap.putAll(databaseImportService.importAlbums("/tmp/downloaded_files/album_part_2.csv"));
            System.out.println("album data imported successfully!");

            System.out.println("Importing artist album links...");
            databaseImportService.importArtistAlbumLinks("/tmp/downloaded_files/artist_album_mapping.csv", albumIDMap, artistIDMap);
            System.out.println("artist album links imported successfully!");

            System.out.println("Importing track data part 1...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_1.csv", albumIDMap);
            System.out.println("Importing track data part 2...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_2.csv", albumIDMap);
            System.out.println("Importing track data part 3...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_3.csv", albumIDMap);
            System.out.println("Importing track data part 4...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_4.csv", albumIDMap);
            System.out.println("Importing track data part 5...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_5.csv", albumIDMap);
            System.out.println("Importing track data part 6...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_6.csv", albumIDMap);
            System.out.println("Importing track data part 7...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_7.csv", albumIDMap);
            System.out.println("Importing track data part 8...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_8.csv", albumIDMap);
            System.out.println("Importing track data part 9...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_9.csv", albumIDMap);
            System.out.println("Importing track data part 10...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_10.csv", albumIDMap);
            System.out.println("Importing track data part 11...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_11.csv", albumIDMap);
            System.out.println("Importing track data part 12...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_12.csv", albumIDMap);
            System.out.println("Importing track data part 13...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_13.csv", albumIDMap);
            System.out.println("Importing track data part 14...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_14.csv", albumIDMap);
            System.out.println("Importing track data part 15...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_15.csv", albumIDMap);
            System.out.println("Importing track data part 16...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_16.csv", albumIDMap);
            System.out.println("Importing track data part 17...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_17.csv", albumIDMap);
            System.out.println("Importing track data part 18...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_18.csv", albumIDMap);
            System.out.println("Importing track data part 19...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_19.csv", albumIDMap);
            System.out.println("Importing track data part 20...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_20.csv", albumIDMap);
            System.out.println("Importing track data part 21...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_21.csv", albumIDMap);
            System.out.println("Importing track data part 22...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_22.csv", albumIDMap);
            System.out.println("Importing track data part 23...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_23.csv", albumIDMap);
            System.out.println("Importing track data part 24...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_24.csv", albumIDMap);
            System.out.println("Importing track data part 25...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_25.csv", albumIDMap);
            System.out.println("Importing track data part 26...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_26.csv", albumIDMap);
            System.out.println("Track data imported successfully!");

            System.out.println("importing genres...");
            databaseImportService.importGenres("/tmp/downloaded_files/spotify_genre_entity_data.csv", artistIDMap);
            System.out.println("Genres imported successfully!");

            System.out.println("importing related artists...");
            databaseImportService.importRelatedArtists("/tmp/downloaded_files/related_artists_data.csv");
            System.out.println("Related artists imported successfully!");

            System.out.println("importing contributors part 1...");
            databaseImportService.importContributors("tmp/downloaded_files/contributor_part_1.csv");
            System.out.println("importing contributors part 2...");
            databaseImportService.importContributors("tmp/downloaded_files/contributor_part_2.csv");
            System.out.println("Contributors imported successfully!");

            System.out.println("importing contributor song mappings...");
            databaseImportService.importContributorSongMapping("/tmp/downloaded_files/contributor_song_mapping.csv");
            System.out.println("Contributor song mappings imported successfully!");
            // databaseImportService.importMBAttributions("C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify
            // Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\mb_attributions_table.csv",artistIDMap);

        } catch (Exception e) {
            System.out.println("Error importing data: " + e.getMessage());
        }
    }

    @Value("${drive.login}")
    private String driveLogin; // Remove static

    private static Drive createDriveService(String driveLogin) throws IOException, GeneralSecurityException {
        InputStream serviceAccountStream = new ByteArrayInputStream(driveLogin.getBytes(StandardCharsets.UTF_8));
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        GoogleCredentials credentials = ServiceAccountCredentials
            .fromStream(serviceAccountStream)
            .createScoped(Collections.singleton(DriveScopes.DRIVE));
        return new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, new HttpCredentialsAdapter(credentials))
            .setApplicationName("Drive API Download Files")
            .build();
    }

    private static String downloadFile(Drive service, String fileID, String outputDirectory) throws IOException {
        File fileMetadata = service.files().get(fileID).setFields("name").execute();
        String fileName = fileMetadata.getName();
        java.io.File outputFile = new java.io.File(outputDirectory, fileName);

        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            HttpResponse response = service.files().get(fileID).executeMedia();
            response.download(fos);
        }

        return fileName;
    }
}
