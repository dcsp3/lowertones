import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISpotifyGenreEntity } from '../spotify-genre-entity.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../spotify-genre-entity.test-samples';

import { SpotifyGenreEntityService } from './spotify-genre-entity.service';

const requireRestSample: ISpotifyGenreEntity = {
  ...sampleWithRequiredData,
};

describe('SpotifyGenreEntity Service', () => {
  let service: SpotifyGenreEntityService;
  let httpMock: HttpTestingController;
  let expectedResult: ISpotifyGenreEntity | ISpotifyGenreEntity[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SpotifyGenreEntityService);
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

    it('should create a SpotifyGenreEntity', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const spotifyGenreEntity = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(spotifyGenreEntity).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SpotifyGenreEntity', () => {
      const spotifyGenreEntity = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(spotifyGenreEntity).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SpotifyGenreEntity', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SpotifyGenreEntity', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SpotifyGenreEntity', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSpotifyGenreEntityToCollectionIfMissing', () => {
      it('should add a SpotifyGenreEntity to an empty array', () => {
        const spotifyGenreEntity: ISpotifyGenreEntity = sampleWithRequiredData;
        expectedResult = service.addSpotifyGenreEntityToCollectionIfMissing([], spotifyGenreEntity);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(spotifyGenreEntity);
      });

      it('should not add a SpotifyGenreEntity to an array that contains it', () => {
        const spotifyGenreEntity: ISpotifyGenreEntity = sampleWithRequiredData;
        const spotifyGenreEntityCollection: ISpotifyGenreEntity[] = [
          {
            ...spotifyGenreEntity,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSpotifyGenreEntityToCollectionIfMissing(spotifyGenreEntityCollection, spotifyGenreEntity);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SpotifyGenreEntity to an array that doesn't contain it", () => {
        const spotifyGenreEntity: ISpotifyGenreEntity = sampleWithRequiredData;
        const spotifyGenreEntityCollection: ISpotifyGenreEntity[] = [sampleWithPartialData];
        expectedResult = service.addSpotifyGenreEntityToCollectionIfMissing(spotifyGenreEntityCollection, spotifyGenreEntity);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(spotifyGenreEntity);
      });

      it('should add only unique SpotifyGenreEntity to an array', () => {
        const spotifyGenreEntityArray: ISpotifyGenreEntity[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const spotifyGenreEntityCollection: ISpotifyGenreEntity[] = [sampleWithRequiredData];
        expectedResult = service.addSpotifyGenreEntityToCollectionIfMissing(spotifyGenreEntityCollection, ...spotifyGenreEntityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const spotifyGenreEntity: ISpotifyGenreEntity = sampleWithRequiredData;
        const spotifyGenreEntity2: ISpotifyGenreEntity = sampleWithPartialData;
        expectedResult = service.addSpotifyGenreEntityToCollectionIfMissing([], spotifyGenreEntity, spotifyGenreEntity2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(spotifyGenreEntity);
        expect(expectedResult).toContain(spotifyGenreEntity2);
      });

      it('should accept null and undefined values', () => {
        const spotifyGenreEntity: ISpotifyGenreEntity = sampleWithRequiredData;
        expectedResult = service.addSpotifyGenreEntityToCollectionIfMissing([], null, spotifyGenreEntity, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(spotifyGenreEntity);
      });

      it('should return initial array if no SpotifyGenreEntity is added', () => {
        const spotifyGenreEntityCollection: ISpotifyGenreEntity[] = [sampleWithRequiredData];
        expectedResult = service.addSpotifyGenreEntityToCollectionIfMissing(spotifyGenreEntityCollection, undefined, null);
        expect(expectedResult).toEqual(spotifyGenreEntityCollection);
      });
    });

    describe('compareSpotifyGenreEntity', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSpotifyGenreEntity(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSpotifyGenreEntity(entity1, entity2);
        const compareResult2 = service.compareSpotifyGenreEntity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSpotifyGenreEntity(entity1, entity2);
        const compareResult2 = service.compareSpotifyGenreEntity(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSpotifyGenreEntity(entity1, entity2);
        const compareResult2 = service.compareSpotifyGenreEntity(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
