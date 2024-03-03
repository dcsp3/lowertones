import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IContributor } from '../contributor.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../contributor.test-samples';

import { ContributorService } from './contributor.service';

const requireRestSample: IContributor = {
  ...sampleWithRequiredData,
};

describe('Contributor Service', () => {
  let service: ContributorService;
  let httpMock: HttpTestingController;
  let expectedResult: IContributor | IContributor[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ContributorService);
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

    it('should create a Contributor', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const contributor = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(contributor).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Contributor', () => {
      const contributor = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(contributor).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Contributor', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Contributor', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Contributor', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addContributorToCollectionIfMissing', () => {
      it('should add a Contributor to an empty array', () => {
        const contributor: IContributor = sampleWithRequiredData;
        expectedResult = service.addContributorToCollectionIfMissing([], contributor);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contributor);
      });

      it('should not add a Contributor to an array that contains it', () => {
        const contributor: IContributor = sampleWithRequiredData;
        const contributorCollection: IContributor[] = [
          {
            ...contributor,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addContributorToCollectionIfMissing(contributorCollection, contributor);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Contributor to an array that doesn't contain it", () => {
        const contributor: IContributor = sampleWithRequiredData;
        const contributorCollection: IContributor[] = [sampleWithPartialData];
        expectedResult = service.addContributorToCollectionIfMissing(contributorCollection, contributor);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contributor);
      });

      it('should add only unique Contributor to an array', () => {
        const contributorArray: IContributor[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const contributorCollection: IContributor[] = [sampleWithRequiredData];
        expectedResult = service.addContributorToCollectionIfMissing(contributorCollection, ...contributorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const contributor: IContributor = sampleWithRequiredData;
        const contributor2: IContributor = sampleWithPartialData;
        expectedResult = service.addContributorToCollectionIfMissing([], contributor, contributor2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contributor);
        expect(expectedResult).toContain(contributor2);
      });

      it('should accept null and undefined values', () => {
        const contributor: IContributor = sampleWithRequiredData;
        expectedResult = service.addContributorToCollectionIfMissing([], null, contributor, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contributor);
      });

      it('should return initial array if no Contributor is added', () => {
        const contributorCollection: IContributor[] = [sampleWithRequiredData];
        expectedResult = service.addContributorToCollectionIfMissing(contributorCollection, undefined, null);
        expect(expectedResult).toEqual(contributorCollection);
      });
    });

    describe('compareContributor', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareContributor(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareContributor(entity1, entity2);
        const compareResult2 = service.compareContributor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareContributor(entity1, entity2);
        const compareResult2 = service.compareContributor(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareContributor(entity1, entity2);
        const compareResult2 = service.compareContributor(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
