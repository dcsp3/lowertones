import com.utils.DbFunctions;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Path filePath = Path.of("C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\spotify_proj\\src\\main\\resources\\smoothsounds4thasoul.json");
        PlaylistIngester ingester = new PlaylistIngester();
        ingester.insert_playlist(filePath);
        //DbFunctions test = new DbFunctions();
        //Connection conn = test.connect();
        //PreparedStatement songStatement = conn.prepareStatement("CREATE TABLE songs(id SERIAL NOT NULL UNIQUE PRIMARY KEY, songTitle VARCHAR(255) NOT NULL, spotifyURI VARCHAR(22) UNIQUE NOT NULL, geniusID VARCHAR(16) UNIQUE, artistsName VARCHAR(255)[] NOT NULL, artistsURI VARCHAR(255)[] NOT NULL, artistsID BIGINT[], producersName VARCHAR(255)[], producersID BIGINT[], writers VARCHAR(255)[], albumTitle VARCHAR(255) NOT NULL, albumURI VARCHAR(255) NOT NULL, releaseDate DATE, popularity INT NOT NULL, explicit BOOLEAN NOT NULL, duration INT NOT NULL);");
        //int affectedRows = songStatement.executeUpdate();
        //test.updateWithGenius(conn, 631);
        //System.out.println(result.getGeniusID());
        //System.out.println(Arrays.toString(result.getProducersName()));
        //System.out.println(Arrays.toString(result.getWritersName()));

        //"SELECT * from songs WHERE geniusID = null"
        /*
        // Specify the directory where your files are located
        String directoryPath = "C:\\Users\\Music\\Desktop\\PROJECTS\\Spotify Project\\";

        // Define the base file name
        String baseFileName = "playlist_items_";

        // Define the number of files you want to iterate through
        int numberOfFiles = 94;  // Adjust this number as needed

        // Iterate through the list of files
        for (int i = 1; i <= numberOfFiles; i++) {
            // Construct the file path for each iteration
            String fileName = baseFileName + i + ".json";
            Path filePath = Paths.get(directoryPath, fileName);

            // Process the file using your existing code
            PlaylistIngester ingester = new PlaylistIngester();
            ingester.insert_playlist(filePath);
        
        }
        */
    }
}
