import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SongArtistJoinDetailComponent } from './song-artist-join-detail.component';

describe('SongArtistJoin Management Detail Component', () => {
  let comp: SongArtistJoinDetailComponent;
  let fixture: ComponentFixture<SongArtistJoinDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SongArtistJoinDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ songArtistJoin: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SongArtistJoinDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SongArtistJoinDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load songArtistJoin on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.songArtistJoin).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
