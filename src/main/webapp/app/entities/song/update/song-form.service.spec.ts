import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../song.test-samples';

import { SongFormService } from './song-form.service';

describe('Song Form Service', () => {
  let service: SongFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SongFormService);
  });

  describe('Service methods', () => {
    describe('createSongFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSongFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            songSpotifyID: expect.any(Object),
            songTitle: expect.any(Object),
            songDuration: expect.any(Object),
            songAlbumType: expect.any(Object),
            songAlbumID: expect.any(Object),
            songExplicit: expect.any(Object),
            songPopularity: expect.any(Object),
            songPreviewURL: expect.any(Object),
            songTrackFeaturesAdded: expect.any(Object),
            songAcousticness: expect.any(Object),
            songDanceability: expect.any(Object),
            songEnergy: expect.any(Object),
            songInstrumentalness: expect.any(Object),
            songLiveness: expect.any(Object),
            songLoudness: expect.any(Object),
            songSpeechiness: expect.any(Object),
            songTempo: expect.any(Object),
            songValence: expect.any(Object),
            songKey: expect.any(Object),
            songTimeSignature: expect.any(Object),
            songDateAddedToDB: expect.any(Object),
            songDateLastModified: expect.any(Object),
            album: expect.any(Object),
          })
        );
      });

      it('passing ISong should create a new form with FormGroup', () => {
        const formGroup = service.createSongFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            songSpotifyID: expect.any(Object),
            songTitle: expect.any(Object),
            songDuration: expect.any(Object),
            songAlbumType: expect.any(Object),
            songAlbumID: expect.any(Object),
            songExplicit: expect.any(Object),
            songPopularity: expect.any(Object),
            songPreviewURL: expect.any(Object),
            songTrackFeaturesAdded: expect.any(Object),
            songAcousticness: expect.any(Object),
            songDanceability: expect.any(Object),
            songEnergy: expect.any(Object),
            songInstrumentalness: expect.any(Object),
            songLiveness: expect.any(Object),
            songLoudness: expect.any(Object),
            songSpeechiness: expect.any(Object),
            songTempo: expect.any(Object),
            songValence: expect.any(Object),
            songKey: expect.any(Object),
            songTimeSignature: expect.any(Object),
            songDateAddedToDB: expect.any(Object),
            songDateLastModified: expect.any(Object),
            album: expect.any(Object),
          })
        );
      });
    });

    describe('getSong', () => {
      it('should return NewSong for default Song initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSongFormGroup(sampleWithNewData);

        const song = service.getSong(formGroup) as any;

        expect(song).toMatchObject(sampleWithNewData);
      });

      it('should return NewSong for empty Song initial value', () => {
        const formGroup = service.createSongFormGroup();

        const song = service.getSong(formGroup) as any;

        expect(song).toMatchObject({});
      });

      it('should return ISong', () => {
        const formGroup = service.createSongFormGroup(sampleWithRequiredData);

        const song = service.getSong(formGroup) as any;

        expect(song).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISong should not enable id FormControl', () => {
        const formGroup = service.createSongFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSong should disable id FormControl', () => {
        const formGroup = service.createSongFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
