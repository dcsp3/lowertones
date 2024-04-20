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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;
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
        "1x_xGab6-YkzXIOLlfxg7JbiE41HO-gF2",
        "1Qnm-_9asISA892kR1vbWi0WNnEwDFSiF",
        "1X4Mv5sszyDDR01k2OyyZsObJgMLHvZQC",
        "1iZZwM6Qqaqy8-sEZI6QdVONJQjYwx7PR",
        "1Go8m2VbUH2Pyr8-XhJFzSicNbr5PwXNx",
        "1hug_mcHZBN4MV8ndu_hGJYyv2YjkCUL4",
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
        // Here you can place the script or call to a method you want to run

        //lol
        //if (albumRepository.count() < 150)
        runCustomScript();
    }

    private void runCustomScript() {
        // Implement your script here
        System.out.println("Running custom script...");
        if (activeProfile.contains("prod")) {
            //some shit
            //download
            Drive service = null;
            try {
                service = createDriveService();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (GeneralSecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String outputDirectory = "./downloaded_files";
            java.io.File fileOutputDir = new java.io.File(outputDirectory);
            if (!fileOutputDir.exists()) {
                fileOutputDir.mkdirs();
            }

            for (String fileID : FILE_IDS) {
                String fileName = null;
                try {
                    fileName = downloadFile(service, fileID, outputDirectory);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println("File " + fileName + " has been downloaded and saved in " + outputDirectory);
            }
            //run import
            //C:\Users\Music\team_project\team37

        } else if (activeProfile.contains("dev")) {
            //run import
            try {
                Map<Long, Long> artistIDMap = databaseImportService.importArtists(
                    "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\artists_table.csv"
                );
                Map<Long, Long> albumIDMap = databaseImportService.importAlbums(
                    "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\album_table.csv"
                );
                databaseImportService.importArtistAlbumLinks(
                    "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\artist_album_mapping.csv",
                    albumIDMap,
                    artistIDMap
                );
                databaseImportService.importTracks(
                    "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\tracks_split\\tracks_part_1.csv",
                    albumIDMap
                );
                databaseImportService.importTracks(
                    "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\tracks_split\\tracks_part_2.csv",
                    albumIDMap
                );
                databaseImportService.importTracks(
                    "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\tracks_split\\tracks_part_3.csv",
                    albumIDMap
                );
                databaseImportService.importTracks(
                    "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\tracks_split\\tracks_part_4.csv",
                    albumIDMap
                );
                databaseImportService.importTracks(
                    "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\tracks_split\\tracks_part_5.csv",
                    albumIDMap
                );
                databaseImportService.importTracks(
                    "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\tracks_split\\tracks_part_6.csv",
                    albumIDMap
                );
                databaseImportService.importTracks(
                    "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\tracks_split\\tracks_part_7.csv",
                    albumIDMap
                );
                databaseImportService.importTracks(
                    "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\tracks_split\\tracks_part_8.csv",
                    albumIDMap
                );
                databaseImportService.importTracks(
                    "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\tracks_split\\tracks_part_9.csv",
                    albumIDMap
                );
                databaseImportService.importTracks(
                    "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\tracks_split\\tracks_part_10.csv",
                    albumIDMap
                );
                databaseImportService.importTracks(
                    "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\tracks_split\\tracks_part_11.csv",
                    albumIDMap
                );
                databaseImportService.importTracks(
                    "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\tracks_split\\tracks_part_12.csv",
                    albumIDMap
                );
                databaseImportService.importTracks(
                    "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\tracks_split\\tracks_part_13.csv",
                    albumIDMap
                );
                databaseImportService.importTracks(
                    "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\tracks_split\\tracks_part_14.csv",
                    albumIDMap
                );
                databaseImportService.importGenres(
                    "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\SPOTIFY_GENRE_ENTITY.csv",
                    artistIDMap
                );
                databaseImportService.importRelatedArtists(
                    "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\related_artists.csv"
                );
                //databaseImportService.importMBAttributions("C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\mb_attributions_table.csv", artistIDMap);
                //databaseImportService.importSongArtistLinks???
                //databaseImportService.importSongGenreLinks???
                //databaseImportService.importContributor("C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\SCRAPED_DATA\\FINISHED\\NEWEST\\contributors_table.csv");

            } catch (Exception e) {
                // TODO: handle exception
            }
        } else {
            System.out.println("wtf");
        }
    }

    private static Drive createDriveService() throws IOException, GeneralSecurityException {
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        FileInputStream serviceAccountStream = new FileInputStream(SERVICE_ACCOUNT_FILE);
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
