import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'spotify-exchange-code',
        data: { pageTitle: 'SpotifyExchangeCodes' },
        loadChildren: () => import('./spotify-exchange-code/spotify-exchange-code.module').then(m => m.SpotifyExchangeCodeModule),
      },
      {
        path: 'song',
        data: { pageTitle: 'Songs' },
        loadChildren: () => import('./song/song.module').then(m => m.SongModule),
      },
      {
        path: 'album',
        data: { pageTitle: 'Albums' },
        loadChildren: () => import('./album/album.module').then(m => m.AlbumModule),
      },
      {
        path: 'main-artist',
        data: { pageTitle: 'MainArtists' },
        loadChildren: () => import('./main-artist/main-artist.module').then(m => m.MainArtistModule),
      },
      {
        path: 'contributor',
        data: { pageTitle: 'Contributors' },
        loadChildren: () => import('./contributor/contributor.module').then(m => m.ContributorModule),
      },
      {
        path: 'music-brainz-song-attribution',
        data: { pageTitle: 'MusicBrainzSongAttributions' },
        loadChildren: () =>
          import('./music-brainz-song-attribution/music-brainz-song-attribution.module').then(m => m.MusicBrainzSongAttributionModule),
      },
      {
        path: 'app-user',
        data: { pageTitle: 'AppUsers' },
        loadChildren: () => import('./app-user/app-user.module').then(m => m.AppUserModule),
      },
      {
        path: 'playlist',
        data: { pageTitle: 'Playlists' },
        loadChildren: () => import('./playlist/playlist.module').then(m => m.PlaylistModule),
      },
      {
        path: 'spotify-genre-entity',
        data: { pageTitle: 'SpotifyGenreEntities' },
        loadChildren: () => import('./spotify-genre-entity/spotify-genre-entity.module').then(m => m.SpotifyGenreEntityModule),
      },
      {
        path: 'musicbrainz-genre-entity',
        data: { pageTitle: 'MusicbrainzGenreEntities' },
        loadChildren: () => import('./musicbrainz-genre-entity/musicbrainz-genre-entity.module').then(m => m.MusicbrainzGenreEntityModule),
      },
      {
        path: 'playlist-song-join',
        data: { pageTitle: 'PlaylistSongJoins' },
        loadChildren: () => import('./playlist-song-join/playlist-song-join.module').then(m => m.PlaylistSongJoinModule),
      },
      {
        path: 'song-artist-join',
        data: { pageTitle: 'SongArtistJoins' },
        loadChildren: () => import('./song-artist-join/song-artist-join.module').then(m => m.SongArtistJoinModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
