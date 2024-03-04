import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../music-brainz-song-attribution.test-samples';

import { MusicBrainzSongAttributionFormService } from './music-brainz-song-attribution-form.service';

describe('MusicBrainzSongAttribution Form Service', () => {
  let service: MusicBrainzSongAttributionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MusicBrainzSongAttributionFormService);
  });

  describe('Service methods', () => {
    describe('createMusicBrainzSongAttributionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMusicBrainzSongAttributionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            recordingMBID: expect.any(Object),
            recordingTitle: expect.any(Object),
            songMainArtistName: expect.any(Object),
            songMainArtistID: expect.any(Object),
            songContributorMBID: expect.any(Object),
            songContributorName: expect.any(Object),
            songContributorRole: expect.any(Object),
            songContributorInstrument: expect.any(Object),
          })
        );
      });

      it('passing IMusicBrainzSongAttribution should create a new form with FormGroup', () => {
        const formGroup = service.createMusicBrainzSongAttributionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            recordingMBID: expect.any(Object),
            recordingTitle: expect.any(Object),
            songMainArtistName: expect.any(Object),
            songMainArtistID: expect.any(Object),
            songContributorMBID: expect.any(Object),
            songContributorName: expect.any(Object),
            songContributorRole: expect.any(Object),
            songContributorInstrument: expect.any(Object),
          })
        );
      });
    });

    describe('getMusicBrainzSongAttribution', () => {
      it('should return NewMusicBrainzSongAttribution for default MusicBrainzSongAttribution initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createMusicBrainzSongAttributionFormGroup(sampleWithNewData);

        const musicBrainzSongAttribution = service.getMusicBrainzSongAttribution(formGroup) as any;

        expect(musicBrainzSongAttribution).toMatchObject(sampleWithNewData);
      });

      it('should return NewMusicBrainzSongAttribution for empty MusicBrainzSongAttribution initial value', () => {
        const formGroup = service.createMusicBrainzSongAttributionFormGroup();

        const musicBrainzSongAttribution = service.getMusicBrainzSongAttribution(formGroup) as any;

        expect(musicBrainzSongAttribution).toMatchObject({});
      });

      it('should return IMusicBrainzSongAttribution', () => {
        const formGroup = service.createMusicBrainzSongAttributionFormGroup(sampleWithRequiredData);

        const musicBrainzSongAttribution = service.getMusicBrainzSongAttribution(formGroup) as any;

        expect(musicBrainzSongAttribution).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMusicBrainzSongAttribution should not enable id FormControl', () => {
        const formGroup = service.createMusicBrainzSongAttributionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMusicBrainzSongAttribution should disable id FormControl', () => {
        const formGroup = service.createMusicBrainzSongAttributionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
