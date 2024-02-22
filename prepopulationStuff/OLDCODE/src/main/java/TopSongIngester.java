import com.datatypes.Song;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.utils.DbFunctions;
import com.utils.Json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class TopSongIngester {
    public static void insert_playlist(Path filePath) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        DbFunctions util = new DbFunctions();
        Connection conn = util.connect();
        String content;
        {
            try {
                content = Files.readString(filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        JsonNode node, items;
        {
            try {
                node = Json.parse(content);
                items = node.path("items");
                System.out.println(items.size());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }
        DateTimeFormatter format = new DateTimeFormatterBuilder()
                .appendPattern("yyyy")
                .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                .toFormatter();

        //iterates through all the items in the playlist
        for (int i = 0; i < items.size(); i++) {
            //formatting artist, song and album title to account for single quotes
            int tempNumOfArtists = items.get(i).path("artists").size();
            String[] artistTemp = new String[tempNumOfArtists];
            String[] artistURITemp = new String[tempNumOfArtists];
            String regexTarget = "'";
            String songTitleTemp = items.get(i).path("name").asText();
            String albumTitleTemp = items.get(i).path("album").path("name").asText();
            String cleanedSongTile = songTitleTemp.replaceAll(regexTarget, "''");
            String cleanedAlbumTile = albumTitleTemp.replaceAll(regexTarget, "''");
            for (int j = 0; j < items.get(i).path("artists").size(); j++) {
                artistTemp[j] = items.get(i).path("artists").get(j).path("name").asText().replaceAll(regexTarget, "''");
                artistURITemp[j] = items.get(i).path("artists").get(j).path("id").asText();
            }
            //formatting date
            LocalDate tempRelease;
            if(items.get(i).path("album").path("release_date_precision").asText().equals("year")) {
                tempRelease = LocalDate.parse(items.get(i).path("album").path("release_date").asText(), format);
            } else if(items.get(i).path("album").path("release_date_precision").asText().equals("day")){
                tempRelease = LocalDate.parse(items.get(i).path("album").path("release_date").asText());
            } else {
                tempRelease = LocalDate.parse("0001-01-01");
            }
            //passing into constructor
            Song temp = new Song(
                    cleanedSongTile,
                    items.get(i).path("id").asText(),
                    artistTemp,
                    artistURITemp,
                    cleanedAlbumTile,
                    items.get(i).path("album").path("id").asText(),
                    tempRelease,
                    items.get(i).path("popularity").asInt(),
                    items.get(i).path("explicit").asBoolean(),
                    items.get(i).path("duration_ms").asInt()
            );
            //inserting song
            util.insert_song(conn, "songs", temp);
        }
    }
}
