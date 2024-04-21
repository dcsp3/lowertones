import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IVault } from '../vault.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../vault.test-samples';

import { VaultService } from './vault.service';

const requireRestSample: IVault = {
  ...sampleWithRequiredData,
};

describe('Vault Service', () => {
  let service: VaultService;
  let httpMock: HttpTestingController;
  let expectedResult: IVault | IVault[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VaultService);
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

    it('should create a Vault', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const vault = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(vault).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Vault', () => {
      const vault = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(vault).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Vault', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Vault', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Vault', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVaultToCollectionIfMissing', () => {
      it('should add a Vault to an empty array', () => {
        const vault: IVault = sampleWithRequiredData;
        expectedResult = service.addVaultToCollectionIfMissing([], vault);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vault);
      });

      it('should not add a Vault to an array that contains it', () => {
        const vault: IVault = sampleWithRequiredData;
        const vaultCollection: IVault[] = [
          {
            ...vault,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVaultToCollectionIfMissing(vaultCollection, vault);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Vault to an array that doesn't contain it", () => {
        const vault: IVault = sampleWithRequiredData;
        const vaultCollection: IVault[] = [sampleWithPartialData];
        expectedResult = service.addVaultToCollectionIfMissing(vaultCollection, vault);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vault);
      });

      it('should add only unique Vault to an array', () => {
        const vaultArray: IVault[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const vaultCollection: IVault[] = [sampleWithRequiredData];
        expectedResult = service.addVaultToCollectionIfMissing(vaultCollection, ...vaultArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vault: IVault = sampleWithRequiredData;
        const vault2: IVault = sampleWithPartialData;
        expectedResult = service.addVaultToCollectionIfMissing([], vault, vault2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vault);
        expect(expectedResult).toContain(vault2);
      });

      it('should accept null and undefined values', () => {
        const vault: IVault = sampleWithRequiredData;
        expectedResult = service.addVaultToCollectionIfMissing([], null, vault, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vault);
      });

      it('should return initial array if no Vault is added', () => {
        const vaultCollection: IVault[] = [sampleWithRequiredData];
        expectedResult = service.addVaultToCollectionIfMissing(vaultCollection, undefined, null);
        expect(expectedResult).toEqual(vaultCollection);
      });
    });

    describe('compareVault', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVault(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareVault(entity1, entity2);
        const compareResult2 = service.compareVault(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareVault(entity1, entity2);
        const compareResult2 = service.compareVault(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareVault(entity1, entity2);
        const compareResult2 = service.compareVault(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
