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
        "1yvfzKwXk76Tt1juzvN3gzK2BClHdh-aC",
        "1KWIlGCkjOq5CvCRdPFRa63rH96rxk2PP",
        "1lD5LWiCn_N3BsQ-aDckt2NHHFumFLvV_",
        "16GD7tqluTms1lWmC9kIawjAMR2w7p8vi",
        "1wPNCJ2rNdOraMxeYmXlKUGdo6s3KlLzz",
        "1AeT5ZzHnGkoAgD64EXaVPkHA8eqmTUYa",
        "1ws7pDG-GXylmgRpkZa0E_U5_y3-k_lq-",
        "12NeB3tcsqrx3BJHj4EHVJz-nzPq9Ak_H",
        "15qL7JRpaP-1Mt_lGXEuefiIWcD8aFgaE",
        "1u9DUcWMkYo_bETMayKhrrb6X9EnaMvl-",
        "1qXY2NKpIgpAjJdGiU6YFAabFZvX4jYYW",
        "1KC5Zfes1RZT5S38MoACN37-zJht_aQDq",
        "1xgEKW5PqgCzgnM4EEQyNcwIx_7OOXlb2",
        "11VhS3QEtAoAsNn5RpFEPJFTKYcg48lRG",
        "117ZdHqqqotLcxhzaKVmCHDeJxXKhOzte",
        "1JGcSAOYMku_Dw_ieCxRpSGfYAHnMfCho",
        "18Qxk5cjdE-XMzdu3IUAhZCzUhq2VgWX9",
        "1-Gvt-ZPiBbXUEuh6w32M62HnsZDhbPEP",
        "1rsFFrZKDX1i_98MYsx1U379nc1P3KeEM",
        "1nsPTRp4siIRRQPD1qzMTOn2PHGnY-vE_",
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

            String outputDirectory = "/tmp/downloaded_files";
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

            runImport();
        } else if (activeProfile.contains("dev")) {
            try {
                // if local change files to local path
                // runImport();
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
            Map<Long, Long> artistIDMap = databaseImportService.importArtists("/tmp/downloaded_files/artists_table.csv");
            System.out.println("artist data imported successfully!");
            System.out.println("Importing album data...");
            Map<Long, Long> albumIDMap = databaseImportService.importAlbums("/tmp/downloaded_files/album_table.csv");
            System.out.println("album data imported successfully!");
            System.out.println("Importing artist album links...");
            databaseImportService.importArtistAlbumLinks("/tmp/downloaded_files/artist_album_mapping.csv", albumIDMap, artistIDMap);
            System.out.println("artist album links imported successfully!");
            System.out.println("Importing track data part 1...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_0.csv", albumIDMap);
            System.out.println("Importing track data part 2...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_1.csv", albumIDMap);
            System.out.println("Importing track data part 3...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_2.csv", albumIDMap);
            System.out.println("Importing track data part 4...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_3.csv", albumIDMap);
            System.out.println("Importing track data part 5...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_4.csv", albumIDMap);
            System.out.println("Importing track data part 6...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_5.csv", albumIDMap);
            System.out.println("Importing track data part 7...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_6.csv", albumIDMap);
            System.out.println("Importing track data part 8...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_7.csv", albumIDMap);
            System.out.println("Importing track data part 9...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_8.csv", albumIDMap);
            System.out.println("Importing track data part 10...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_9.csv", albumIDMap);
            System.out.println("Importing track data part 11...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_10.csv", albumIDMap);
            System.out.println("Importing track data part 12...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_11.csv", albumIDMap);
            System.out.println("Importing track data part 13...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_12.csv", albumIDMap);
            System.out.println("Importing track data part 14...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_13.csv", albumIDMap);
            System.out.println("Importing track data part 15...");
            databaseImportService.importTracks("/tmp/downloaded_files/tracks_part_14.csv", albumIDMap);
            System.out.println("Track data imported successfully!");
            System.out.println("importing genres...");
            databaseImportService.importGenres("/tmp/downloaded_files/SPOTIFY_GENRE_ENTITY.csv", artistIDMap);
            System.out.println("Genres imported successfully!");
            System.out.println("importing related artists...");
            databaseImportService.importRelatedArtists("/tmp/downloaded_files/related_artists.csv");
            System.out.println("Related artists imported successfully!");
            // databaseImportService.importMBAttributions("C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify
            // Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\mb_attributions_table.csv",
            // artistIDMap);
            // databaseImportService.importSongArtistLinks???
            // databaseImportService.importSongGenreLinks???
            // databaseImportService.importContributor("C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify
            // Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\contributors_table.csv");
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
