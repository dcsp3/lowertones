import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMusicBrainzSongAttribution } from '../music-brainz-song-attribution.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../music-brainz-song-attribution.test-samples';

import { MusicBrainzSongAttributionService } from './music-brainz-song-attribution.service';

const requireRestSample: IMusicBrainzSongAttribution = {
  ...sampleWithRequiredData,
};

describe('MusicBrainzSongAttribution Service', () => {
  let service: MusicBrainzSongAttributionService;
  let httpMock: HttpTestingController;
  let expectedResult: IMusicBrainzSongAttribution | IMusicBrainzSongAttribution[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MusicBrainzSongAttributionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a MusicBrainzSongAttribution', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const musicBrainzSongAttribution = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(musicBrainzSongAttribution).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MusicBrainzSongAttribution', () => {
      const musicBrainzSongAttribution = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(musicBrainzSongAttribution).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MusicBrainzSongAttribution', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MusicBrainzSongAttribution', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MusicBrainzSongAttribution', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMusicBrainzSongAttributionToCollectionIfMissing', () => {
      it('should add a MusicBrainzSongAttribution to an empty array', () => {
        const musicBrainzSongAttribution: IMusicBrainzSongAttribution = sampleWithRequiredData;
        expectedResult = service.addMusicBrainzSongAttributionToCollectionIfMissing([], musicBrainzSongAttribution);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(musicBrainzSongAttribution);
      });

      it('should not add a MusicBrainzSongAttribution to an array that contains it', () => {
        const musicBrainzSongAttribution: IMusicBrainzSongAttribution = sampleWithRequiredData;
        const musicBrainzSongAttributionCollection: IMusicBrainzSongAttribution[] = [
          {
            ...musicBrainzSongAttribution,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMusicBrainzSongAttributionToCollectionIfMissing(
          musicBrainzSongAttributionCollection,
          musicBrainzSongAttribution
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MusicBrainzSongAttribution to an array that doesn't contain it", () => {
        const musicBrainzSongAttribution: IMusicBrainzSongAttribution = sampleWithRequiredData;
        const musicBrainzSongAttributionCollection: IMusicBrainzSongAttribution[] = [sampleWithPartialData];
        expectedResult = service.addMusicBrainzSongAttributionToCollectionIfMissing(
          musicBrainzSongAttributionCollection,
          musicBrainzSongAttribution
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(musicBrainzSongAttribution);
      });

      it('should add only unique MusicBrainzSongAttribution to an array', () => {
        const musicBrainzSongAttributionArray: IMusicBrainzSongAttribution[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const musicBrainzSongAttributionCollection: IMusicBrainzSongAttribution[] = [sampleWithRequiredData];
        expectedResult = service.addMusicBrainzSongAttributionToCollectionIfMissing(
          musicBrainzSongAttributionCollection,
          ...musicBrainzSongAttributionArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const musicBrainzSongAttribution: IMusicBrainzSongAttribution = sampleWithRequiredData;
        const musicBrainzSongAttribution2: IMusicBrainzSongAttribution = sampleWithPartialData;
        expectedResult = service.addMusicBrainzSongAttributionToCollectionIfMissing(
          [],
          musicBrainzSongAttribution,
          musicBrainzSongAttribution2
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(musicBrainzSongAttribution);
        expect(expectedResult).toContain(musicBrainzSongAttribution2);
      });

      it('should accept null and undefined values', () => {
        const musicBrainzSongAttribution: IMusicBrainzSongAttribution = sampleWithRequiredData;
        expectedResult = service.addMusicBrainzSongAttributionToCollectionIfMissing([], null, musicBrainzSongAttribution, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(musicBrainzSongAttribution);
      });

      it('should return initial array if no MusicBrainzSongAttribution is added', () => {
        const musicBrainzSongAttributionCollection: IMusicBrainzSongAttribution[] = [sampleWithRequiredData];
        expectedResult = service.addMusicBrainzSongAttributionToCollectionIfMissing(musicBrainzSongAttributionCollection, undefined, null);
        expect(expectedResult).toEqual(musicBrainzSongAttributionCollection);
      });
    });

    describe('compareMusicBrainzSongAttribution', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMusicBrainzSongAttribution(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMusicBrainzSongAttribution(entity1, entity2);
        const compareResult2 = service.compareMusicBrainzSongAttribution(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMusicBrainzSongAttribution(entity1, entity2);
        const compareResult2 = service.compareMusicBrainzSongAttribution(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMusicBrainzSongAttribution(entity1, entity2);
        const compareResult2 = service.compareMusicBrainzSongAttribution(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
