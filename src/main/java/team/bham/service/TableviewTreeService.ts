import { Injectable } from '@angular/core';

@Injectable()
export class TableviewTreeService {
  getTreeNodesData() {
    return [
      {
        key: '0',
        label: 'Playlists',
        tooltip: 'Select the tracks you want to use in the Working Playlist',
        data: 'PlaylistSelect',
        icon: 'pi pi-fw pi-heart',
        children: [
          {
            key: '0-1',
            label: 'PlaylistSelection',
            data: 'PlaylistSelection',
            type: 'PlaylistElement',
          },
        ],
      },
      {
        key: '1',
        label: 'Search Type',
        tooltip:
          'Select how you want your search to be applied:\n' +
          '\nTitles & Artists - Search is applied to both titles and artists' +
          '\nTitles - Search is applied to titles only' +
          '\nArtists - Search is applied to artists only',
        data: 'SearchType',
        icon: 'pi pi-fw pi-search',
        children: [
          {
            key: '1-1',
            label: 'SearchTypeSelection',
            data: 'SearchTypeSelection',
            type: 'SearchElement',
          },
        ],
      },
      {
        key: '2',
        label: 'Track Duration',
        tooltip: 'Filter by track duration',
        data: 'TrackDuration',
        icon: 'pi pi-fw pi-hourglass',
        children: [
          {
            key: '2-1',
            label: 'TrackDuration',
            data: 'TrackDuration',
            type: 'DurationElement',
          },
        ],
      },
      {
        key: '3',
        label: 'Release Years',
        tooltip: 'Filter by track release years',
        data: 'ReleaseYears',
        icon: 'pi pi-fw pi-calendar',
        children: [
          {
            key: '3-1',
            label: 'Years',
            data: 'Years',
            type: 'YearsElement',
          },
        ],
      },
      {
        key: '4',
        label: 'Popularity',
        tooltip:
          "Filter by track popularity\nThis metric is calculated by Spotify. The artist's\npopularity is calculated from the popularity\n of all the artist's tracks",
        data: 'Popularity',
        icon: 'pi pi-fw pi-globe',
        children: [
          {
            key: '4-1',
            label: 'Popularity',
            data: 'Popularity',
            type: 'PopularityElement',
          },
        ],
      },
      {
        key: '5',
        label: 'Tempo',
        tooltip: 'Filter by track tempo',
        data: 'Tempo',
        icon: 'pi pi-fw pi-stopwatch',
        children: [
          {
            key: '5-1',
            label: 'Tempo',
            data: 'Tempo',
            type: 'TempoElement',
          },
        ],
      },
      {
        key: '6',
        label: 'Track Features',
        tooltip:
          'Filter by track features\nThese metrics are calculated by Spotify.' +
          '\nAcousticness - A measure from 0-100 of whether the track is acoustic.' +
          '\nDanceability - A measure from 0-100 of how suitable a track is for dancing.' +
          '\nInstrumentalness - A measure from 0-100 of how instrumental a track is.' +
          '\nEnergy - A measure from 0-100 of how intense and energetic a track is.' +
          '\nLiveness - A measure from 0-100 of the presence of the live audience in the recording.' +
          '\nLoudness - A measure from -60-0 of how loud a song is in dB.' +
          '\nSpeechiness - A measure from 0-100 of the presence of spoken word in the track.' +
          '\nValence - A measure from 0-100 conveying the musical positiveness in the track.',
        data: 'Track Features',
        icon: 'pi pi-fw pi-list',
        children: [
          {
            key: '6-1',
            label: 'Track Features',
            data: 'Track Features',
            type: 'FeaturesElement',
          },
        ],
      },
      {
        key: '7',
        label: 'Artist Search',
        tooltip: "Filter by multiple artists\nPress Enter to confirm artist's name",
        data: 'ArtistSearch',
        icon: 'pi pi-fw pi-users',
        children: [
          {
            key: '7-1',
            label: 'ArtistChip',
            data: 'ArtistChip',
            type: 'ArtistChipElement',
          },
        ],
      },
      {
        key: '8',
        label: 'Contributor Search',
        tooltip: "Filter by multiple contributors\nPress Enter to confirm contributor's name",
        data: 'ContributorSearch',
        icon: 'pi pi-fw pi-tag',
        children: [
          {
            key: '8-1',
            label: 'ContributorChip',
            data: 'ContributorChip',
            type: 'ContributorChipElement',
          },
        ],
      },
      {
        key: '9',
        label: 'Explicitness',
        tooltip:
          'Filter by specific explicitness\n' +
          '\nBoth - Both Explicit and Non-Explicit tracks will be shown' +
          '\nExplicit - Only Explicit tracks will be shown' +
          '\nNon-Explicit - only Non-Explicit tracks will be shown',
        data: 'Explicitness',
        icon: 'pi pi-fw pi-exclamation-triangle',
        children: [
          {
            key: '9-1',
            label: 'Explicit',
            data: 'Explicitness',
            type: 'ExplicitnessElement',
          },
        ],
      },
      {
        key: '10',
        label: 'Column Selection',
        tooltip: 'Select which columns are shown in the table',
        data: 'ColumnSelection',
        icon: 'pi pi-fw pi-table',
        children: [
          {
            key: '10-1',
            label: 'ColumnSelectionList',
            data: 'ColumnSelectionList',
            type: 'ColumnSelectionListElement',
          },
        ],
      },
    ];
  }

  getTreeNodes() {
    return Promise.resolve(this.getTreeNodesData());
  }
}
