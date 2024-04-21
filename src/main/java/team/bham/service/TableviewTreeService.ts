import { Injectable } from '@angular/core';

@Injectable()
export class TableviewTreeService {
  getTreeNodesData() {
    return [
      {
        key: '0',
        label: 'Playlists',
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
        label: 'Song Duration',
        data: 'SongDuration',
        icon: 'pi pi-fw pi-hourglass',
        children: [
          {
            key: '2-1',
            label: 'SongDuration',
            data: 'SongDuration',
            type: 'DurationElement',
          },
        ],
      },
      {
        key: '3',
        label: 'Release Years',
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
        label: 'Song Features',
        data: 'Song Features',
        icon: 'pi pi-fw pi-list',
        children: [
          {
            key: '6-1',
            label: 'Song Features',
            data: 'Song Features',
            type: 'FeaturesElement',
          },
        ],
      },
      {
        key: '7',
        label: 'Artist Search',
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
        label: 'Producer Search',
        data: 'ProducerSearch',
        icon: 'pi pi-fw pi-tag',
        children: [
          {
            key: '8-1',
            label: 'ProducerChip',
            data: 'ProducerChip',
            type: 'ProducerChipElement',
          },
        ],
      },
      {
        key: '9',
        label: 'Explicitness',
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
