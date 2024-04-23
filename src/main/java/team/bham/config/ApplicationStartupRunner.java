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
        "1qXXWnNcyfSEFeeJ1How11bY8QiWJDkkT",
        "1K6mjK69grqvMv6J-Wb6ra5tdePEeY_GB",
        "19WAvky8Gwe_3eo0AGipNRogsFS5ZQhth",
        "1UtN9son_AZ3X5q0t9zFtrl3uSXKnV4z-",
        "1QBGgdEtkIcq0znJdJKECHriNgFgckUIO",
        "1hCzb7caSxKGphkJYNeoadhiy7t8XPgKp",
        "1W_ynQoNyHx4C9Q_I_agJN8qFhPj2wgM7",
        "1MyeCUa98Mms41PV0k6mqeexE7gESshSe",
        "1-WBbQX6fZEk2NERN_g_kfRAqxWk1EDfP",
        "1iGCXDKhTdTJrNAEqlV8TcxzV8Ro_eHh3",
        "15ES-u_jApE_vnGIc2mUpUiKR-tQXfEiG",
        "1n5zAOBoMxMwUVKXQS5wcqv6bCYa_qQmP",
        "1LaWfYdvo-4hfzFjzCH35ff3j8uciPdqO",
        "1-fsEwPk_Afy0YSRMKwB8Z1pWDuwMyCwK",
        "1KcAgg79_L452Jd2zF_tLQfEZmtDje3PL",
        "1lLMlumvPnUr8jVrIWj0_VDUWcnyjCKVW",
        "1XsYLOaXGO_nU33413rCnIH5e_Yp9HhxZ",
        "1WEwidxq3Q6xdwA0IhNSj38xK_zp80ntF",
        "152s5A9ZxJpA4d99HgYFBX3E4iWS6VdXg",
        "1Kk9Cy4anzIN861GvQCrbAzbhILjw4HJm",
        "1Hzi4XNf6rAhp2A01_gIPrzFJKtbnRWIS",
        "1gQvU70BfI0UXLkgueaS66o0rnYTHo33I",
        "1-FGSWY1HK9kitvDsVb2V8ussj6KB6kQk",
        "15gVSlpVxSDDtE7ANhijAFd_I9tY-Jocp",
        "14BZ0OVnYOSjpYObt7auMFjt1o6jl3s26",
        "1BvVURLhPLm4LQiwB9rZ7YuL2lNS5VmbW",
        "1dk7XvOUvuTeEkvXoVSAk9zP5q9o1bvQC",
        "19yv3Dp02fdu21mJtKtLtllLRjZhfmMkT",
        "1Jr2kOKigf7iPWpMEhoJ8WbghEpXr0gE1",
        "1njyUIAy-45-rw0okiiBDLXmNKRHQJxhm",
        "1buPJI6k--0T7s9hm1wSjzN73YfU42q7r",
        "1pXC96LImehWGrJfyiNwDAp8Eb3xrjT3O",
        "1kf67RfkkypP-rbkrgUTpzu158I9Jjb6L",
        "",
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
        // Your custom logic here
        System.out.println("Application is ready and live!");

        System.out.println("Current working directory: " + System.getProperty("user.dir"));

        // Here you can place the script or call to a method you want to run

        // lol
        // if (albumRepository.count() < 150)
        runCustomScript();
    }

    private void runCustomScript() {
        // Implement your script here
        System.out.println("Running custom script...");
        if (activeProfile.contains("prod")) {
            // some shit
            // download
            Drive service = null;
            try {
                service = createDriveService(driveLogin);
                System.out.println("1");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.out.println("2");

                e.printStackTrace();
            } catch (GeneralSecurityException e) {
                // TODO Auto-generated catch block
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
                    String[] command = { "ls", "-al" };

                    return; // Exit the method if the directory cannot be created
                }
            }
            try {
                // Command to list files in the current directory
                String[] command = { "ls", "-al" };

                // Create ProcessBuilder
                ProcessBuilder processBuilder = new ProcessBuilder(command);

                // Start the process
                Process process = processBuilder.start();

                // Read output
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }

                // Wait for the process to finish
                int exitCode = process.waitFor();
                System.out.println("\nExited with error code : " + exitCode);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("4");

            for (String fileID : FILE_IDS) {
                String fileName = null;
                System.out.println("5");

                try {
                    fileName = downloadFile(service, fileID, outputDirectory);
                    System.out.println("6");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println("File " + fileName + " has been downloaded and saved in " + outputDirectory);
                System.out.println("6");
            }
            System.out.println("7");
            System.out.println("7");
            System.out.println("7");
            System.out.println("7");

            //wait 10 seconds
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // SO PEOPLE CAN TEST DEV WHILE IMPORT NOT WORKING
            //runImport();

        } else if (activeProfile.contains("dev")) {
            try {
                // if local change files to local path
                //runImport();
            } catch (Exception e) {
                // TODO: handle exception
            }
        } else {
            System.out.println("wtf");
        }
    }

    private void runImport() {
        // run import
        try {
            System.out.println("8");
            System.out.println("Importing artist data...");
            Map<Long, Long> artistIDMap = databaseImportService.importArtists(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\artist_data.csv"
            );
            System.out.println("artist data imported successfully!");

            System.out.println("Importing album data part 1...");
            Map<Long, Long> albumIDMap = databaseImportService.importAlbums(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\album_full.csv"
            );
            //System.out.println("Importing album data part 2...");
            //albumIDMap.putAll(databaseImportService.importAlbums("C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\album_part_2.csv"));
            System.out.println("album data imported successfully!");

            System.out.println("Importing artist album links...");
            databaseImportService.importArtistAlbumLinks(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\artist_album_mapping.csv",
                albumIDMap,
                artistIDMap
            );
            System.out.println("artist album links imported successfully!");

            System.out.println("Importing track data part 1...");
            databaseImportService.importTracks(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\tracks_part_1.csv",
                albumIDMap
            );
            System.out.println("Importing track data part 2...");
            databaseImportService.importTracks(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\tracks_part_2.csv",
                albumIDMap
            );
            System.out.println("Importing track data part 3...");
            databaseImportService.importTracks(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\tracks_part_3.csv",
                albumIDMap
            );
            System.out.println("Importing track data part 4...");
            databaseImportService.importTracks(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\tracks_part_4.csv",
                albumIDMap
            );
            System.out.println("Importing track data part 5...");
            databaseImportService.importTracks(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\tracks_part_5.csv",
                albumIDMap
            );
            System.out.println("Importing track data part 6...");
            databaseImportService.importTracks(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\tracks_part_6.csv",
                albumIDMap
            );
            System.out.println("Importing track data part 7...");
            databaseImportService.importTracks(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\tracks_part_7.csv",
                albumIDMap
            );
            System.out.println("Importing track data part 8...");
            databaseImportService.importTracks(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\tracks_part_8.csv",
                albumIDMap
            );
            System.out.println("Importing track data part 9...");
            databaseImportService.importTracks(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\tracks_part_9.csv",
                albumIDMap
            );
            System.out.println("Importing track data part 10...");
            databaseImportService.importTracks(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\tracks_part_10.csv",
                albumIDMap
            );
            System.out.println("Importing track data part 11...");
            databaseImportService.importTracks(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\tracks_part_11.csv",
                albumIDMap
            );
            System.out.println("Importing track data part 12...");
            databaseImportService.importTracks(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\tracks_part_12.csv",
                albumIDMap
            );
            System.out.println("Importing track data part 13...");
            databaseImportService.importTracks(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\tracks_part_13.csv",
                albumIDMap
            );
            System.out.println("Importing track data part 14...");
            databaseImportService.importTracks(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\tracks_part_14.csv",
                albumIDMap
            );
            System.out.println("Importing track data part 15...");
            databaseImportService.importTracks(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\tracks_part_15.csv",
                albumIDMap
            );
            System.out.println("Importing track data part 16...");
            databaseImportService.importTracks(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\tracks_part_16.csv",
                albumIDMap
            );
            System.out.println("Importing track data part 17...");
            databaseImportService.importTracks(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\tracks_part_17.csv",
                albumIDMap
            );
            System.out.println("Importing track data part 18...");
            databaseImportService.importTracks(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\tracks_part_18.csv",
                albumIDMap
            );
            System.out.println("Importing track data part 19...");
            databaseImportService.importTracks(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\tracks_part_19.csv",
                albumIDMap
            );
            System.out.println("Importing track data part 20...");
            databaseImportService.importTracks(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\tracks_part_20.csv",
                albumIDMap
            );
            System.out.println("Importing track data part 21...");
            databaseImportService.importTracks(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\tracks_part_21.csv",
                albumIDMap
            );
            System.out.println("Importing track data part 22...");
            databaseImportService.importTracks(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\tracks_part_22.csv",
                albumIDMap
            );
            System.out.println("Importing track data part 23...");
            databaseImportService.importTracks(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\tracks_part_23.csv",
                albumIDMap
            );
            System.out.println("Importing track data part 24...");
            databaseImportService.importTracks(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\tracks_part_24.csv",
                albumIDMap
            );
            System.out.println("Importing track data part 25...");
            databaseImportService.importTracks(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\tracks_part_25.csv",
                albumIDMap
            );
            System.out.println("Importing track data part 26...");
            databaseImportService.importTracks(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\tracks_part_26.csv",
                albumIDMap
            );
            System.out.println("Track data imported successfully!");

            System.out.println("importing genres...");
            databaseImportService.importGenres(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\spotify_genre_entity_data.csv",
                artistIDMap
            );
            System.out.println("Genres imported successfully!");

            System.out.println("importing related artists...");
            databaseImportService.importRelatedArtists(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\related_artists_data.csv"
            );
            System.out.println("Related artists imported successfully!");

            System.out.println("importing contributors part 1...");
            databaseImportService.importContributors("tmp/downloaded_files/contributor_part_1.csv");
            System.out.println("importing contributors part 2...");
            databaseImportService.importContributors("tmp/downloaded_files/contributor_part_2.csv");
            System.out.println("Contributors imported successfully!");

            System.out.println("importing contributor song mappings...");
            databaseImportService.importContributorSongMapping(
                "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\PRODFILES\\contributor_song_mapping.csv"
            );
            System.out.println("Contributor song mappings imported successfully!");
            // databaseImportService.importMBAttributions("C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\mb_attributions_table.csv",artistIDMap);

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
