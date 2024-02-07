import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISpotifyExchangeCode } from '../spotify-exchange-code.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../spotify-exchange-code.test-samples';

import { SpotifyExchangeCodeService } from './spotify-exchange-code.service';

const requireRestSample: ISpotifyExchangeCode = {
  ...sampleWithRequiredData,
};

describe('SpotifyExchangeCode Service', () => {
  let service: SpotifyExchangeCodeService;
  let httpMock: HttpTestingController;
  let expectedResult: ISpotifyExchangeCode | ISpotifyExchangeCode[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SpotifyExchangeCodeService);
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

    it('should create a SpotifyExchangeCode', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const spotifyExchangeCode = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(spotifyExchangeCode).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SpotifyExchangeCode', () => {
      const spotifyExchangeCode = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(spotifyExchangeCode).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SpotifyExchangeCode', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SpotifyExchangeCode', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SpotifyExchangeCode', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSpotifyExchangeCodeToCollectionIfMissing', () => {
      it('should add a SpotifyExchangeCode to an empty array', () => {
        const spotifyExchangeCode: ISpotifyExchangeCode = sampleWithRequiredData;
        expectedResult = service.addSpotifyExchangeCodeToCollectionIfMissing([], spotifyExchangeCode);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(spotifyExchangeCode);
      });

      it('should not add a SpotifyExchangeCode to an array that contains it', () => {
        const spotifyExchangeCode: ISpotifyExchangeCode = sampleWithRequiredData;
        const spotifyExchangeCodeCollection: ISpotifyExchangeCode[] = [
          {
            ...spotifyExchangeCode,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSpotifyExchangeCodeToCollectionIfMissing(spotifyExchangeCodeCollection, spotifyExchangeCode);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SpotifyExchangeCode to an array that doesn't contain it", () => {
        const spotifyExchangeCode: ISpotifyExchangeCode = sampleWithRequiredData;
        const spotifyExchangeCodeCollection: ISpotifyExchangeCode[] = [sampleWithPartialData];
        expectedResult = service.addSpotifyExchangeCodeToCollectionIfMissing(spotifyExchangeCodeCollection, spotifyExchangeCode);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(spotifyExchangeCode);
      });

      it('should add only unique SpotifyExchangeCode to an array', () => {
        const spotifyExchangeCodeArray: ISpotifyExchangeCode[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const spotifyExchangeCodeCollection: ISpotifyExchangeCode[] = [sampleWithRequiredData];
        expectedResult = service.addSpotifyExchangeCodeToCollectionIfMissing(spotifyExchangeCodeCollection, ...spotifyExchangeCodeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const spotifyExchangeCode: ISpotifyExchangeCode = sampleWithRequiredData;
        const spotifyExchangeCode2: ISpotifyExchangeCode = sampleWithPartialData;
        expectedResult = service.addSpotifyExchangeCodeToCollectionIfMissing([], spotifyExchangeCode, spotifyExchangeCode2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(spotifyExchangeCode);
        expect(expectedResult).toContain(spotifyExchangeCode2);
      });

      it('should accept null and undefined values', () => {
        const spotifyExchangeCode: ISpotifyExchangeCode = sampleWithRequiredData;
        expectedResult = service.addSpotifyExchangeCodeToCollectionIfMissing([], null, spotifyExchangeCode, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(spotifyExchangeCode);
      });

      it('should return initial array if no SpotifyExchangeCode is added', () => {
        const spotifyExchangeCodeCollection: ISpotifyExchangeCode[] = [sampleWithRequiredData];
        expectedResult = service.addSpotifyExchangeCodeToCollectionIfMissing(spotifyExchangeCodeCollection, undefined, null);
        expect(expectedResult).toEqual(spotifyExchangeCodeCollection);
      });
    });

    describe('compareSpotifyExchangeCode', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSpotifyExchangeCode(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSpotifyExchangeCode(entity1, entity2);
        const compareResult2 = service.compareSpotifyExchangeCode(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSpotifyExchangeCode(entity1, entity2);
        const compareResult2 = service.compareSpotifyExchangeCode(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSpotifyExchangeCode(entity1, entity2);
        const compareResult2 = service.compareSpotifyExchangeCode(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
