import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMusicbrainzGenreEntity } from '../musicbrainz-genre-entity.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../musicbrainz-genre-entity.test-samples';

import { MusicbrainzGenreEntityService } from './musicbrainz-genre-entity.service';

const requireRestSample: IMusicbrainzGenreEntity = {
  ...sampleWithRequiredData,
};

describe('MusicbrainzGenreEntity Service', () => {
  let service: MusicbrainzGenreEntityService;
  let httpMock: HttpTestingController;
  let expectedResult: IMusicbrainzGenreEntity | IMusicbrainzGenreEntity[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MusicbrainzGenreEntityService);
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

    it('should create a MusicbrainzGenreEntity', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const musicbrainzGenreEntity = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(musicbrainzGenreEntity).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MusicbrainzGenreEntity', () => {
      const musicbrainzGenreEntity = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(musicbrainzGenreEntity).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MusicbrainzGenreEntity', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MusicbrainzGenreEntity', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MusicbrainzGenreEntity', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMusicbrainzGenreEntityToCollectionIfMissing', () => {
      it('should add a MusicbrainzGenreEntity to an empty array', () => {
        const musicbrainzGenreEntity: IMusicbrainzGenreEntity = sampleWithRequiredData;
        expectedResult = service.addMusicbrainzGenreEntityToCollectionIfMissing([], musicbrainzGenreEntity);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(musicbrainzGenreEntity);
      });

      it('should not add a MusicbrainzGenreEntity to an array that contains it', () => {
        const musicbrainzGenreEntity: IMusicbrainzGenreEntity = sampleWithRequiredData;
        const musicbrainzGenreEntityCollection: IMusicbrainzGenreEntity[] = [
          {
            ...musicbrainzGenreEntity,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMusicbrainzGenreEntityToCollectionIfMissing(musicbrainzGenreEntityCollection, musicbrainzGenreEntity);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MusicbrainzGenreEntity to an array that doesn't contain it", () => {
        const musicbrainzGenreEntity: IMusicbrainzGenreEntity = sampleWithRequiredData;
        const musicbrainzGenreEntityCollection: IMusicbrainzGenreEntity[] = [sampleWithPartialData];
        expectedResult = service.addMusicbrainzGenreEntityToCollectionIfMissing(musicbrainzGenreEntityCollection, musicbrainzGenreEntity);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(musicbrainzGenreEntity);
      });

      it('should add only unique MusicbrainzGenreEntity to an array', () => {
        const musicbrainzGenreEntityArray: IMusicbrainzGenreEntity[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const musicbrainzGenreEntityCollection: IMusicbrainzGenreEntity[] = [sampleWithRequiredData];
        expectedResult = service.addMusicbrainzGenreEntityToCollectionIfMissing(
          musicbrainzGenreEntityCollection,
          ...musicbrainzGenreEntityArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const musicbrainzGenreEntity: IMusicbrainzGenreEntity = sampleWithRequiredData;
        const musicbrainzGenreEntity2: IMusicbrainzGenreEntity = sampleWithPartialData;
        expectedResult = service.addMusicbrainzGenreEntityToCollectionIfMissing([], musicbrainzGenreEntity, musicbrainzGenreEntity2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(musicbrainzGenreEntity);
        expect(expectedResult).toContain(musicbrainzGenreEntity2);
      });

      it('should accept null and undefined values', () => {
        const musicbrainzGenreEntity: IMusicbrainzGenreEntity = sampleWithRequiredData;
        expectedResult = service.addMusicbrainzGenreEntityToCollectionIfMissing([], null, musicbrainzGenreEntity, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(musicbrainzGenreEntity);
      });

      it('should return initial array if no MusicbrainzGenreEntity is added', () => {
        const musicbrainzGenreEntityCollection: IMusicbrainzGenreEntity[] = [sampleWithRequiredData];
        expectedResult = service.addMusicbrainzGenreEntityToCollectionIfMissing(musicbrainzGenreEntityCollection, undefined, null);
        expect(expectedResult).toEqual(musicbrainzGenreEntityCollection);
      });
    });

    describe('compareMusicbrainzGenreEntity', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMusicbrainzGenreEntity(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMusicbrainzGenreEntity(entity1, entity2);
        const compareResult2 = service.compareMusicbrainzGenreEntity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMusicbrainzGenreEntity(entity1, entity2);
        const compareResult2 = service.compareMusicbrainzGenreEntity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMusicbrainzGenreEntity(entity1, entity2);
        const compareResult2 = service.compareMusicbrainzGenreEntity(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
