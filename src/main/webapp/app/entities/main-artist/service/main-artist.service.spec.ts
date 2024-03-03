import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IMainArtist } from '../main-artist.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../main-artist.test-samples';

import { MainArtistService, RestMainArtist } from './main-artist.service';

const requireRestSample: RestMainArtist = {
  ...sampleWithRequiredData,
  dateAddedToDB: sampleWithRequiredData.dateAddedToDB?.format(DATE_FORMAT),
  dateLastModified: sampleWithRequiredData.dateLastModified?.format(DATE_FORMAT),
};

describe('MainArtist Service', () => {
  let service: MainArtistService;
  let httpMock: HttpTestingController;
  let expectedResult: IMainArtist | IMainArtist[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MainArtistService);
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

    it('should create a MainArtist', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const mainArtist = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(mainArtist).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MainArtist', () => {
      const mainArtist = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(mainArtist).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MainArtist', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MainArtist', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MainArtist', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMainArtistToCollectionIfMissing', () => {
      it('should add a MainArtist to an empty array', () => {
        const mainArtist: IMainArtist = sampleWithRequiredData;
        expectedResult = service.addMainArtistToCollectionIfMissing([], mainArtist);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mainArtist);
      });

      it('should not add a MainArtist to an array that contains it', () => {
        const mainArtist: IMainArtist = sampleWithRequiredData;
        const mainArtistCollection: IMainArtist[] = [
          {
            ...mainArtist,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMainArtistToCollectionIfMissing(mainArtistCollection, mainArtist);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MainArtist to an array that doesn't contain it", () => {
        const mainArtist: IMainArtist = sampleWithRequiredData;
        const mainArtistCollection: IMainArtist[] = [sampleWithPartialData];
        expectedResult = service.addMainArtistToCollectionIfMissing(mainArtistCollection, mainArtist);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mainArtist);
      });

      it('should add only unique MainArtist to an array', () => {
        const mainArtistArray: IMainArtist[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const mainArtistCollection: IMainArtist[] = [sampleWithRequiredData];
        expectedResult = service.addMainArtistToCollectionIfMissing(mainArtistCollection, ...mainArtistArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const mainArtist: IMainArtist = sampleWithRequiredData;
        const mainArtist2: IMainArtist = sampleWithPartialData;
        expectedResult = service.addMainArtistToCollectionIfMissing([], mainArtist, mainArtist2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mainArtist);
        expect(expectedResult).toContain(mainArtist2);
      });

      it('should accept null and undefined values', () => {
        const mainArtist: IMainArtist = sampleWithRequiredData;
        expectedResult = service.addMainArtistToCollectionIfMissing([], null, mainArtist, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mainArtist);
      });

      it('should return initial array if no MainArtist is added', () => {
        const mainArtistCollection: IMainArtist[] = [sampleWithRequiredData];
        expectedResult = service.addMainArtistToCollectionIfMissing(mainArtistCollection, undefined, null);
        expect(expectedResult).toEqual(mainArtistCollection);
      });
    });

    describe('compareMainArtist', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMainArtist(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMainArtist(entity1, entity2);
        const compareResult2 = service.compareMainArtist(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMainArtist(entity1, entity2);
        const compareResult2 = service.compareMainArtist(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMainArtist(entity1, entity2);
        const compareResult2 = service.compareMainArtist(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
