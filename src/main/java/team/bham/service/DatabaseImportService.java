package team.bham.service;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.bham.domain.Album;
import team.bham.domain.MainArtist;
import team.bham.domain.enumeration.AlbumType;
import team.bham.domain.enumeration.ReleaseDatePrecision;
import team.bham.repository.*;

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

    @Transactional
    public Map<Long, Long> importAlbums(String filePath) throws IOException {
        Map<Long, Long> idMapping = new HashMap<>();
        try (Reader in = new FileReader(filePath)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader().parse(in);

            for (CSVRecord record : records) {
                Album album = new Album()
                    .albumSpotifyID(record.get("album_spotify_id"))
                    .albumName(record.get("album_name"))
                    .albumCoverArt(record.get("album_cover_art"))
                    .albumReleaseDate(LocalDate.parse(record.get("album_release_date")))
                    .releaseDatePrecision(ReleaseDatePrecision.valueOf(record.get("release_date_precision").toUpperCase()))
                    .albumPopularity(Integer.parseInt(record.get("album_popularity")))
                    .albumType(AlbumType.valueOf(record.get("album_type").toUpperCase()))
                    .spotifyAlbumUPC(record.get("spotify_album_upc"))
                    .spotifyAlbumEAN(record.get("spotify_album_ean"))
                    .spotifyAlbumISRC(record.get("spotify_album_isrc"))
                    .dateAddedToDB(LocalDate.parse(record.get("date_added_to_db")))
                    .dateLastModified(LocalDate.parse(record.get("date_last_modified")))
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
        try (Reader in = new FileReader(filePath)) {
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
        Reader in = new FileReader(filePath);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader().parse(in);
        for (CSVRecord record : records) {
            Long oldAlbumId = Long.valueOf(record.get("albumID"));
            Long oldArtistId = Long.valueOf(record.get("artistID"));
            Long newAlbumId = albumIdMapping.get(oldAlbumId);
            Long newArtistId = artistIdMapping.get(oldArtistId);

            Album album = albumRepository.findById(newAlbumId).orElseThrow();
            MainArtist artist = mainArtistRepository.findById(newArtistId).orElseThrow();
            artist.addAlbum(album);
            mainArtistRepository.save(artist);
        }
    }
}
