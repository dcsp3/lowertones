import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISongArtistJoin } from '../song-artist-join.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../song-artist-join.test-samples';

import { SongArtistJoinService } from './song-artist-join.service';

const requireRestSample: ISongArtistJoin = {
  ...sampleWithRequiredData,
};

describe('SongArtistJoin Service', () => {
  let service: SongArtistJoinService;
  let httpMock: HttpTestingController;
  let expectedResult: ISongArtistJoin | ISongArtistJoin[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SongArtistJoinService);
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

    it('should create a SongArtistJoin', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const songArtistJoin = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(songArtistJoin).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SongArtistJoin', () => {
      const songArtistJoin = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(songArtistJoin).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SongArtistJoin', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SongArtistJoin', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SongArtistJoin', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSongArtistJoinToCollectionIfMissing', () => {
      it('should add a SongArtistJoin to an empty array', () => {
        const songArtistJoin: ISongArtistJoin = sampleWithRequiredData;
        expectedResult = service.addSongArtistJoinToCollectionIfMissing([], songArtistJoin);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(songArtistJoin);
      });

      it('should not add a SongArtistJoin to an array that contains it', () => {
        const songArtistJoin: ISongArtistJoin = sampleWithRequiredData;
        const songArtistJoinCollection: ISongArtistJoin[] = [
          {
            ...songArtistJoin,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSongArtistJoinToCollectionIfMissing(songArtistJoinCollection, songArtistJoin);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SongArtistJoin to an array that doesn't contain it", () => {
        const songArtistJoin: ISongArtistJoin = sampleWithRequiredData;
        const songArtistJoinCollection: ISongArtistJoin[] = [sampleWithPartialData];
        expectedResult = service.addSongArtistJoinToCollectionIfMissing(songArtistJoinCollection, songArtistJoin);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(songArtistJoin);
      });

      it('should add only unique SongArtistJoin to an array', () => {
        const songArtistJoinArray: ISongArtistJoin[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const songArtistJoinCollection: ISongArtistJoin[] = [sampleWithRequiredData];
        expectedResult = service.addSongArtistJoinToCollectionIfMissing(songArtistJoinCollection, ...songArtistJoinArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const songArtistJoin: ISongArtistJoin = sampleWithRequiredData;
        const songArtistJoin2: ISongArtistJoin = sampleWithPartialData;
        expectedResult = service.addSongArtistJoinToCollectionIfMissing([], songArtistJoin, songArtistJoin2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(songArtistJoin);
        expect(expectedResult).toContain(songArtistJoin2);
      });

      it('should accept null and undefined values', () => {
        const songArtistJoin: ISongArtistJoin = sampleWithRequiredData;
        expectedResult = service.addSongArtistJoinToCollectionIfMissing([], null, songArtistJoin, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(songArtistJoin);
      });

      it('should return initial array if no SongArtistJoin is added', () => {
        const songArtistJoinCollection: ISongArtistJoin[] = [sampleWithRequiredData];
        expectedResult = service.addSongArtistJoinToCollectionIfMissing(songArtistJoinCollection, undefined, null);
        expect(expectedResult).toEqual(songArtistJoinCollection);
      });
    });

    describe('compareSongArtistJoin', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSongArtistJoin(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSongArtistJoin(entity1, entity2);
        const compareResult2 = service.compareSongArtistJoin(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSongArtistJoin(entity1, entity2);
        const compareResult2 = service.compareSongArtistJoin(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSongArtistJoin(entity1, entity2);
        const compareResult2 = service.compareSongArtistJoin(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
