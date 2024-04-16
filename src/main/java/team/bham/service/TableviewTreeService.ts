import { Injectable } from '@angular/core';

@Injectable()
export class TableviewTreeService {
  getTreeNodesData() {
    return [
      {
        key: '0',
        label: 'Search Type',
        data: 'SearchType',
        icon: 'pi pi-fw pi-search',
        children: [
          {
            key: '0-1',
            label: 'SearchTypeSelection',
            data: 'SearchTypeSelection',
            type: 'SearchElement',
          },
        ],
      },
      {
        key: '1',
        label: 'Release Years',
        data: 'ReleaseYears',
        icon: 'pi pi-fw pi-calendar',
        children: [
          {
            key: '1-1',
            label: 'Years',
            data: 'Years',
            type: 'YearsElement',
          },
        ],
      },
      {
        key: '2',
        label: 'Popularity',
        data: 'Popularity',
        icon: 'pi pi-fw pi-globe',
        children: [
          {
            key: '2-1',
            label: 'Popularity',
            data: 'Popularity',
            type: 'PopularityElement',
          },
        ],
      },
      {
        key: '3',
        label: 'Artist Search',
        data: 'ArtistSearch',
        icon: 'pi pi-fw pi-users',
        children: [
          {
            key: '3-1',
            label: 'ArtistChip',
            data: 'ArtistChip',
            type: 'ArtistChipElement',
          },
        ],
      },
      {
        key: '4',
        label: 'Producer Search',
        data: 'ProducerSearch',
        icon: 'pi pi-fw pi-users',
        children: [
          {
            key: '4-1',
            label: 'ProducerChip',
            data: 'ProducerChip',
            type: 'ProducerChipElement',
          },
        ],
      },
      {
        key: '5',
        label: 'Column Selection',
        data: 'ColumnSelection',
        icon: 'pi pi-fw pi-table',
        children: [
          {
            key: '4-1',
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
