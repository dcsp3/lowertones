package team.bham.service;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import liquibase.datatype.core.BigIntType;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.bham.domain.Album;
import team.bham.domain.MainArtist;
import team.bham.domain.enumeration.AlbumType;
import team.bham.domain.enumeration.ReleaseDatePrecision;
import team.bham.repository.AlbumRepository;
import team.bham.repository.MainArtistRepository;

@Service
@Transactional
public class DatabaseImportService {

    @PersistenceContext
    private EntityManager entityManager;

    private final AlbumRepository albumRepository;
    private final MainArtistRepository mainArtistRepository;

    public DatabaseImportService(AlbumRepository albumRepository, MainArtistRepository mainArtistRepository) {
        this.albumRepository = albumRepository;
        this.mainArtistRepository = mainArtistRepository;
    }

    private static final DateTimeFormatter ISO_LOCAL_DATE = DateTimeFormatter.ISO_LOCAL_DATE; // yyyy-MM-dd
    private static final DateTimeFormatter YEAR_MONTH_FORMATTER = new DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM")
        .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
        .toFormatter();
    private static final DateTimeFormatter YEAR_FORMATTER = new DateTimeFormatterBuilder()
        .appendPattern("yyyy")
        .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
        .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
        .toFormatter();

    public static LocalDate parseDate(String dateString) {
        if (dateString.startsWith("0000")) {
            dateString = "1900" + dateString.substring(4); // Replace "0000" with "1900"
        }

        try {
            return LocalDate.parse(dateString, ISO_LOCAL_DATE);
        } catch (DateTimeParseException e1) {
            try {
                return LocalDate.parse(dateString, YEAR_MONTH_FORMATTER);
            } catch (DateTimeParseException e2) {
                try {
                    return LocalDate.parse(dateString, YEAR_FORMATTER);
                } catch (DateTimeParseException e3) {
                    throw new DateTimeParseException("Failed to parse date: " + dateString, dateString, 0);
                }
            }
        }
    }

    @Transactional
    public Map<Long, Long> importAlbums(String filePath) throws IOException {
        Map<Long, Long> idMapping = new HashMap<>();
        try (Reader in = new FileReader(filePath, StandardCharsets.UTF_8)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader().parse(in);

            for (CSVRecord record : records) {
                Album album = new Album()
                    .albumSpotifyID(record.get("album_spotify_id"))
                    .albumName(record.get("album_name"))
                    .albumCoverArt(record.get("album_cover_art"))
                    .albumReleaseDate(parseDate(record.get("album_release_date")))
                    .releaseDatePrecision(ReleaseDatePrecision.valueOf(record.get("release_date_precision").toUpperCase()))
                    .albumPopularity(Integer.parseInt(record.get("album_popularity")))
                    .albumType(AlbumType.valueOf(record.get("album_type").toUpperCase()))
                    .spotifyAlbumUPC(record.get("spotify_album_upc"))
                    .spotifyAlbumEAN(record.get("spotify_album_ean"))
                    .spotifyAlbumISRC(record.get("spotify_album_isrc"))
                    .dateAddedToDB(LocalDate.now())
                    .dateLastModified(LocalDate.now())
                    .musicbrainzMetadataAdded(Boolean.parseBoolean(record.get("musicbrainz_metadata_added")))
                    .musicbrainzID(record.get("musicbrainz_id"));
                // entityManager.persist(albumRepository);
                Album savedAlbum = albumRepository.save(album);
                idMapping.put(Long.parseLong(record.get("id")), savedAlbum.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idMapping;
    }

    @Transactional
    public Map<Long, Long> importArtists(String filePath) throws IOException {
        Map<Long, Long> idMapping = new HashMap<>();
        try (Reader in = new FileReader(filePath, StandardCharsets.UTF_8)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader().parse(in);
            for (CSVRecord record : records) {
                MainArtist artist = new MainArtist()
                    .artistSpotifyID(record.get("artist_spotify_id"))
                    .artistName(record.get("artist_name"))
                    .artistPopularity(Integer.valueOf(record.get("artist_popularity")))
                    .artistImageSmall(record.get("artist_image_small"))
                    .artistImageMedium(record.get("artist_image_medium"))
                    .artistImageLarge(record.get("artist_image_large"))
                    .artistFollowers(Integer.valueOf(record.get("artist_followers")))
                    .dateAddedToDB(LocalDate.now())
                    .dateLastModified(LocalDate.now())
                    .musicbrainzID(record.get("musicbrainz_id"));
                MainArtist savedArtist = mainArtistRepository.save(artist);
                idMapping.put(Long.parseLong(record.get("id")), savedArtist.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idMapping;
    }

    @Transactional
    public void importArtistAlbumLinks(String filePath, Map<Long, Long> albumIdMapping, Map<Long, Long> artistIdMapping)
        throws IOException {
        Reader in = new FileReader(filePath, StandardCharsets.UTF_8);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader().parse(in);

        /*
        for (CSVRecord record : records) {
            Long oldAlbumId = Long.valueOf(record.get("albumID"));
            Long oldArtistId = Long.valueOf(record.get("artistID"));
            Long newAlbumId = oldAlbumId+albumIdMappingOffset;
            Long newArtistId = oldArtistId+artistIdMappingOffset;

            Album album = albumRepository.findById(newAlbumId).orElseThrow();
            MainArtist artist = mainArtistRepository.findById(newArtistId).orElseThrow();
            artist.addAlbum(album);
            mainArtistRepository.save(artist);
        }
        */
        for (CSVRecord record : records) {
            Long oldAlbumId = Long.valueOf(record.get("albumID"));
            Long oldArtistId = Long.valueOf(record.get("artistID"));
            Long newAlbumId = albumIdMapping.get(oldAlbumId);
            Long newArtistId = artistIdMapping.get(oldArtistId);
            entityManager
                .createNativeQuery("INSERT INTO REL_ARTISTS_TABLE__ALBUM (ALBUM_ID, ARTISTS_TABLE_ID) VALUES (?, ?)")
                .setParameter(1, newAlbumId)
                .setParameter(2, newArtistId)
                .executeUpdate();
        }
    }

    @Transactional
    public void importTracks(String filePath, Map<Long, Long> albumIdMapping) throws IOException {
        Reader in = new FileReader(filePath, StandardCharsets.UTF_8);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader().parse(in);
        for (CSVRecord record : records) {
            Long oldAlbumId = (long) (Double.parseDouble(record.get("album_id")));
            //Long oldAlbumId = Long.valueOf(record.get("album_id"));
            System.out.println("oldAlbumId: " + oldAlbumId);
            Long newAlbumId = albumIdMapping.get(oldAlbumId);

            Long id = Long.parseLong(record.get("id"));
            Long popularity = Long.parseLong(record.get("song_popularity"));
            Long key = Long.parseLong(record.get("song_key"));
            Long timeSig = Long.parseLong(record.get("song_time_signature"));
            Long duration = Long.parseLong(record.get("song_duration"));

            Float songAcousticness = Float.parseFloat(record.get("song_acousticness"));
            Float songDanceability = Float.parseFloat(record.get("song_danceability"));
            Float songEnergy = Float.parseFloat(record.get("song_energy"));
            Float songInstrumentalness = Float.parseFloat(record.get("song_instrumentalness"));
            Float songLiveness = Float.parseFloat(record.get("song_liveness"));
            Float songLoudness = Float.parseFloat(record.get("song_loudness"));
            Float songSpeechiness = Float.parseFloat(record.get("song_speechiness"));
            Float songTempo = Float.parseFloat(record.get("song_tempo"));
            Float songValence = Float.parseFloat(record.get("song_valence"));

            entityManager
                .createNativeQuery(
                    "INSERT INTO SONG_TABLE (ID, SONG_SPOTIFY_ID, SONG_TITLE, SONG_DURATION, SONG_ALBUM_TYPE, SONG_ALBUM_ID, SONG_EXPLICIT, SONG_POPULARITY, SONG_PREVIEW_URL, SONG_TRACK_FEATURES_ADDED, SONG_ACOUSTICNESS, SONG_DANCEABILITY, SONG_ENERGY, SONG_INSTRUMENTALNESS, SONG_LIVENESS, SONG_LOUDNESS, SONG_SPEECHINESS, SONG_TEMPO, SONG_VALENCE, SONG_KEY, SONG_TIME_SIGNATURE, SONG_DATE_ADDED_TO_DB, SONG_DATE_LAST_MODIFIED, RECORDING_MBID, ALBUM_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                )
                .setParameter(1, id)
                .setParameter(2, record.get("song_spotify_id"))
                .setParameter(3, record.get("song_title"))
                .setParameter(4, duration)
                .setParameter(5, record.get("song_album_type"))
                .setParameter(6, record.get("song_album_id"))
                .setParameter(7, record.get("song_explicit"))
                .setParameter(8, popularity)
                .setParameter(9, record.get("song_preview_url"))
                .setParameter(10, record.get("song_track_features_added"))
                .setParameter(11, songAcousticness)
                .setParameter(12, songDanceability)
                .setParameter(13, songEnergy)
                .setParameter(14, songInstrumentalness)
                .setParameter(15, songLiveness)
                .setParameter(16, songLoudness)
                .setParameter(17, songSpeechiness)
                .setParameter(18, songTempo)
                .setParameter(19, songValence)
                .setParameter(20, key)
                .setParameter(21, timeSig)
                .setParameter(22, LocalDate.now())
                .setParameter(23, LocalDate.now())
                .setParameter(24, record.get("recording_mbid"))
                .setParameter(25, newAlbumId)
                .executeUpdate();
        }
    }

    @Transactional
    public void importGenres(String filePath, Map<Long, Long> artistIdMapping) throws IOException {
        Reader in = new FileReader(filePath, StandardCharsets.UTF_8);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader().parse(in);
        for (CSVRecord record : records) {
            Long oldArtistId = (long) (Double.parseDouble(record.get("MAIN_ARTIST_ID")));
            //Long oldAlbumId = Long.valueOf(record.get("album_id"));
            Long newArtistId = artistIdMapping.get(oldArtistId);
            Long id = Long.parseLong(record.get("ID"));

            entityManager
                .createNativeQuery("INSERT INTO SPOTIFY_GENRE_ENTITY (ID, SPOTIFY_GENRE, MAIN_ARTIST_ID) VALUES (?, ?, ?)")
                .setParameter(1, id)
                .setParameter(2, record.get("SPOTIFY_GENRE"))
                .setParameter(3, newArtistId)
                .executeUpdate();
        }
    }

    public void importRelatedArtists(String filePath) throws IOException {
        Reader in = new FileReader(filePath, StandardCharsets.UTF_8);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader().parse(in);
        for (CSVRecord record : records) {
            Long id = Long.parseLong(record.get("ID"));

            entityManager
                .createNativeQuery(
                    "INSERT INTO RELATED_ARTISTS (ID, MAIN_ARTIST_SPTFY_ID, RELATED_ARTIST_SPOTIFY_ID_1, RELATED_ARTIST_SPOTIFY_ID_2, RELATED_ARTIST_SPOTIFY_ID_3, RELATED_ARTIST_SPOTIFY_ID_4, RELATED_ARTIST_SPOTIFY_ID_5, RELATED_ARTIST_SPOTIFY_ID_6,RELATED_ARTIST_SPOTIFY_ID_7, RELATED_ARTIST_SPOTIFY_ID_8, RELATED_ARTIST_SPOTIFY_ID_9, RELATED_ARTIST_SPOTIFY_ID_10, RELATED_ARTIST_SPOTIFY_ID_11, RELATED_ARTIST_SPOTIFY_ID_12, RELATED_ARTIST_SPOTIFY_ID_13, RELATED_ARTIST_SPOTIFY_ID_14, RELATED_ARTIST_SPOTIFY_ID_15, RELATED_ARTIST_SPOTIFY_ID_16, RELATED_ARTIST_SPOTIFY_ID_17, RELATED_ARTIST_SPOTIFY_ID_18, RELATED_ARTIST_SPOTIFY_ID_19, RELATED_ARTIST_SPOTIFY_ID_20) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                )
                .setParameter(1, id)
                .setParameter(2, record.get("MAIN_ARTIST_SPTFY_ID"))
                .setParameter(3, record.get("RELATED_ARTIST_SPOTIFY_ID_1"))
                .setParameter(4, record.get("RELATED_ARTIST_SPOTIFY_ID_2"))
                .setParameter(5, record.get("RELATED_ARTIST_SPOTIFY_ID_3"))
                .setParameter(6, record.get("RELATED_ARTIST_SPOTIFY_ID_4"))
                .setParameter(7, record.get("RELATED_ARTIST_SPOTIFY_ID_5"))
                .setParameter(8, record.get("RELATED_ARTIST_SPOTIFY_ID_6"))
                .setParameter(9, record.get("RELATED_ARTIST_SPOTIFY_ID_7"))
                .setParameter(10, record.get("RELATED_ARTIST_SPOTIFY_ID_8"))
                .setParameter(11, record.get("RELATED_ARTIST_SPOTIFY_ID_9"))
                .setParameter(12, record.get("RELATED_ARTIST_SPOTIFY_ID_10"))
                .setParameter(13, record.get("RELATED_ARTIST_SPOTIFY_ID_11"))
                .setParameter(14, record.get("RELATED_ARTIST_SPOTIFY_ID_12"))
                .setParameter(15, record.get("RELATED_ARTIST_SPOTIFY_ID_13"))
                .setParameter(16, record.get("RELATED_ARTIST_SPOTIFY_ID_14"))
                .setParameter(17, record.get("RELATED_ARTIST_SPOTIFY_ID_15"))
                .setParameter(18, record.get("RELATED_ARTIST_SPOTIFY_ID_16"))
                .setParameter(19, record.get("RELATED_ARTIST_SPOTIFY_ID_17"))
                .setParameter(20, record.get("RELATED_ARTIST_SPOTIFY_ID_18"))
                .setParameter(21, record.get("RELATED_ARTIST_SPOTIFY_ID_19"))
                .setParameter(22, record.get("RELATED_ARTIST_SPOTIFY_ID_20"))
                .executeUpdate();
        }
    }

    public void importContributors(String filePath) throws IOException {
        Reader in = new FileReader(filePath, StandardCharsets.UTF_8);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader().parse(in);
        for (CSVRecord record : records) {
            Long id = Long.parseLong(record.get("id"));

            entityManager
                .createNativeQuery("INSERT INTO CONTRIBUTOR (ID, NAME, ROLE, INSTRUMENT, MUSICBRAINZ_ID) VALUES (?, ?, ?, ?, ?)")
                .setParameter(1, id)
                .setParameter(2, record.get("individual_artist_name"))
                .setParameter(3, record.get("role"))
                .setParameter(4, record.get("instrument"))
                .setParameter(5, record.get("artist_mbid"))
                .executeUpdate();
        }
    }

    public void importContributorSongMapping(String filePath) throws IOException {
        Reader in = new FileReader(filePath, StandardCharsets.UTF_8);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader().parse(in);
        for (CSVRecord record : records) {
            entityManager
                .createNativeQuery("INSERT INTO REL_SONG_TABLE__CONTRIBUTOR (CONTRIBUTOR_ID, SONG_TABLE_ID) VALUES (?, ?)")
                .setParameter(1, record.get("CONTRIBUTOR_ID"))
                .setParameter(2, record.get("SONG_TABLE_ID"))
                .executeUpdate();
        }
    }
}
