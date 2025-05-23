package team.bham.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, team.bham.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, team.bham.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, team.bham.domain.User.class.getName());
            createCache(cm, team.bham.domain.Authority.class.getName());
            createCache(cm, team.bham.domain.User.class.getName() + ".authorities");
            createCache(cm, team.bham.domain.SpotifyExchangeCode.class.getName());
            createCache(cm, team.bham.domain.Song.class.getName());
            createCache(cm, team.bham.domain.Song.class.getName() + ".contributors");
            createCache(cm, team.bham.domain.Song.class.getName() + ".spotifyGenreEntities");
            createCache(cm, team.bham.domain.Song.class.getName() + ".musicbrainzGenreEntities");
            createCache(cm, team.bham.domain.Song.class.getName() + ".playlistSongJoins");
            createCache(cm, team.bham.domain.Song.class.getName() + ".songArtistJoins");
            createCache(cm, team.bham.domain.Album.class.getName());
            createCache(cm, team.bham.domain.Album.class.getName() + ".songs");
            createCache(cm, team.bham.domain.Album.class.getName() + ".spotifyGenreEntities");
            createCache(cm, team.bham.domain.Album.class.getName() + ".musicbrainzGenreEntities");
            createCache(cm, team.bham.domain.MainArtist.class.getName());
            createCache(cm, team.bham.domain.MainArtist.class.getName() + ".albums");
            createCache(cm, team.bham.domain.MainArtist.class.getName() + ".spotifyGenreEntities");
            createCache(cm, team.bham.domain.MainArtist.class.getName() + ".musicbrainzGenreEntities");
            createCache(cm, team.bham.domain.MainArtist.class.getName() + ".songArtistJoins");
            createCache(cm, team.bham.domain.Contributor.class.getName());
            createCache(cm, team.bham.domain.MusicBrainzSongAttribution.class.getName());
            createCache(cm, team.bham.domain.AppUser.class.getName());
            createCache(cm, team.bham.domain.AppUser.class.getName() + ".playlists");
            createCache(cm, team.bham.domain.Playlist.class.getName());
            createCache(cm, team.bham.domain.Playlist.class.getName() + ".playlistSongJoins");
            createCache(cm, team.bham.domain.SpotifyGenreEntity.class.getName());
            createCache(cm, team.bham.domain.MusicbrainzGenreEntity.class.getName());
            createCache(cm, team.bham.domain.PlaylistSongJoin.class.getName());
            createCache(cm, team.bham.domain.SongArtistJoin.class.getName());
            createCache(cm, team.bham.domain.Contributor.class.getName() + ".songs");
            createCache(cm, team.bham.domain.RelatedArtists.class.getName());
            createCache(cm, team.bham.domain.Album.class.getName() + ".mainArtists");
            createCache(cm, team.bham.domain.Vault.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
