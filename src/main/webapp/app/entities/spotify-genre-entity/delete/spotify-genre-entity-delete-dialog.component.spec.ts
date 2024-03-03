jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { SpotifyGenreEntityService } from '../service/spotify-genre-entity.service';

import { SpotifyGenreEntityDeleteDialogComponent } from './spotify-genre-entity-delete-dialog.component';

describe('SpotifyGenreEntity Management Delete Component', () => {
  let comp: SpotifyGenreEntityDeleteDialogComponent;
  let fixture: ComponentFixture<SpotifyGenreEntityDeleteDialogComponent>;
  let service: SpotifyGenreEntityService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SpotifyGenreEntityDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(SpotifyGenreEntityDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SpotifyGenreEntityDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SpotifyGenreEntityService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
