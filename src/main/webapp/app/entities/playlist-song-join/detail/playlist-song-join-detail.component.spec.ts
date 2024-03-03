import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PlaylistSongJoinDetailComponent } from './playlist-song-join-detail.component';

describe('PlaylistSongJoin Management Detail Component', () => {
  let comp: PlaylistSongJoinDetailComponent;
  let fixture: ComponentFixture<PlaylistSongJoinDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PlaylistSongJoinDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ playlistSongJoin: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PlaylistSongJoinDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PlaylistSongJoinDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load playlistSongJoin on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.playlistSongJoin).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
