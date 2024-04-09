import { Injectable } from '@angular/core';

@Injectable()
export class TableviewTreeService {
  getTreeNodesData() {
    return [
      {
        key: '0',
        label: 'Release Years',
        data: 'ReleaseYears',
        icon: 'pi pi-fw pi-calendar',
        children: [
          {
            key: '0-0',
            label: 'Years',
            data: 'Years',
            type: 'YearsElement',
          },
        ],
      },
      {
        key: '1',
        label: 'Popularity',
        data: 'Popularity',
        icon: 'pi pi-fw pi-users',
        children: [
          {
            key: '1-1',
            label: 'Slider',
            data: 'Slider',
            type: 'SliderElement',
          },
        ],
      },
    ];
  }

  getTreeNodes() {
    return Promise.resolve(this.getTreeNodesData());
  }
}
