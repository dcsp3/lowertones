import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRelatedArtists } from '../related-artists.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../related-artists.test-samples';

import { RelatedArtistsService } from './related-artists.service';

const requireRestSample: IRelatedArtists = {
  ...sampleWithRequiredData,
};

describe('RelatedArtists Service', () => {
  let service: RelatedArtistsService;
  let httpMock: HttpTestingController;
  let expectedResult: IRelatedArtists | IRelatedArtists[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RelatedArtistsService);
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

    it('should create a RelatedArtists', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const relatedArtists = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(relatedArtists).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RelatedArtists', () => {
      const relatedArtists = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(relatedArtists).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RelatedArtists', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RelatedArtists', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a RelatedArtists', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRelatedArtistsToCollectionIfMissing', () => {
      it('should add a RelatedArtists to an empty array', () => {
        const relatedArtists: IRelatedArtists = sampleWithRequiredData;
        expectedResult = service.addRelatedArtistsToCollectionIfMissing([], relatedArtists);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(relatedArtists);
      });

      it('should not add a RelatedArtists to an array that contains it', () => {
        const relatedArtists: IRelatedArtists = sampleWithRequiredData;
        const relatedArtistsCollection: IRelatedArtists[] = [
          {
            ...relatedArtists,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRelatedArtistsToCollectionIfMissing(relatedArtistsCollection, relatedArtists);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RelatedArtists to an array that doesn't contain it", () => {
        const relatedArtists: IRelatedArtists = sampleWithRequiredData;
        const relatedArtistsCollection: IRelatedArtists[] = [sampleWithPartialData];
        expectedResult = service.addRelatedArtistsToCollectionIfMissing(relatedArtistsCollection, relatedArtists);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(relatedArtists);
      });

      it('should add only unique RelatedArtists to an array', () => {
        const relatedArtistsArray: IRelatedArtists[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const relatedArtistsCollection: IRelatedArtists[] = [sampleWithRequiredData];
        expectedResult = service.addRelatedArtistsToCollectionIfMissing(relatedArtistsCollection, ...relatedArtistsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const relatedArtists: IRelatedArtists = sampleWithRequiredData;
        const relatedArtists2: IRelatedArtists = sampleWithPartialData;
        expectedResult = service.addRelatedArtistsToCollectionIfMissing([], relatedArtists, relatedArtists2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(relatedArtists);
        expect(expectedResult).toContain(relatedArtists2);
      });

      it('should accept null and undefined values', () => {
        const relatedArtists: IRelatedArtists = sampleWithRequiredData;
        expectedResult = service.addRelatedArtistsToCollectionIfMissing([], null, relatedArtists, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(relatedArtists);
      });

      it('should return initial array if no RelatedArtists is added', () => {
        const relatedArtistsCollection: IRelatedArtists[] = [sampleWithRequiredData];
        expectedResult = service.addRelatedArtistsToCollectionIfMissing(relatedArtistsCollection, undefined, null);
        expect(expectedResult).toEqual(relatedArtistsCollection);
      });
    });

    describe('compareRelatedArtists', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRelatedArtists(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRelatedArtists(entity1, entity2);
        const compareResult2 = service.compareRelatedArtists(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRelatedArtists(entity1, entity2);
        const compareResult2 = service.compareRelatedArtists(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRelatedArtists(entity1, entity2);
        const compareResult2 = service.compareRelatedArtists(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
