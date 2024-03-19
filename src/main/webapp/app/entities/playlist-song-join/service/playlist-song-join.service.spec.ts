import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPlaylistSongJoin } from '../playlist-song-join.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../playlist-song-join.test-samples';

import { PlaylistSongJoinService, RestPlaylistSongJoin } from './playlist-song-join.service';

const requireRestSample: RestPlaylistSongJoin = {
  ...sampleWithRequiredData,
  songDateAdded: sampleWithRequiredData.songDateAdded?.format(DATE_FORMAT),
};

describe('PlaylistSongJoin Service', () => {
  let service: PlaylistSongJoinService;
  let httpMock: HttpTestingController;
  let expectedResult: IPlaylistSongJoin | IPlaylistSongJoin[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PlaylistSongJoinService);
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

    it('should create a PlaylistSongJoin', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const playlistSongJoin = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(playlistSongJoin).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PlaylistSongJoin', () => {
      const playlistSongJoin = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(playlistSongJoin).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PlaylistSongJoin', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PlaylistSongJoin', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PlaylistSongJoin', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPlaylistSongJoinToCollectionIfMissing', () => {
      it('should add a PlaylistSongJoin to an empty array', () => {
        const playlistSongJoin: IPlaylistSongJoin = sampleWithRequiredData;
        expectedResult = service.addPlaylistSongJoinToCollectionIfMissing([], playlistSongJoin);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(playlistSongJoin);
      });

      it('should not add a PlaylistSongJoin to an array that contains it', () => {
        const playlistSongJoin: IPlaylistSongJoin = sampleWithRequiredData;
        const playlistSongJoinCollection: IPlaylistSongJoin[] = [
          {
            ...playlistSongJoin,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPlaylistSongJoinToCollectionIfMissing(playlistSongJoinCollection, playlistSongJoin);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PlaylistSongJoin to an array that doesn't contain it", () => {
        const playlistSongJoin: IPlaylistSongJoin = sampleWithRequiredData;
        const playlistSongJoinCollection: IPlaylistSongJoin[] = [sampleWithPartialData];
        expectedResult = service.addPlaylistSongJoinToCollectionIfMissing(playlistSongJoinCollection, playlistSongJoin);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(playlistSongJoin);
      });

      it('should add only unique PlaylistSongJoin to an array', () => {
        const playlistSongJoinArray: IPlaylistSongJoin[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const playlistSongJoinCollection: IPlaylistSongJoin[] = [sampleWithRequiredData];
        expectedResult = service.addPlaylistSongJoinToCollectionIfMissing(playlistSongJoinCollection, ...playlistSongJoinArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const playlistSongJoin: IPlaylistSongJoin = sampleWithRequiredData;
        const playlistSongJoin2: IPlaylistSongJoin = sampleWithPartialData;
        expectedResult = service.addPlaylistSongJoinToCollectionIfMissing([], playlistSongJoin, playlistSongJoin2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(playlistSongJoin);
        expect(expectedResult).toContain(playlistSongJoin2);
      });

      it('should accept null and undefined values', () => {
        const playlistSongJoin: IPlaylistSongJoin = sampleWithRequiredData;
        expectedResult = service.addPlaylistSongJoinToCollectionIfMissing([], null, playlistSongJoin, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(playlistSongJoin);
      });

      it('should return initial array if no PlaylistSongJoin is added', () => {
        const playlistSongJoinCollection: IPlaylistSongJoin[] = [sampleWithRequiredData];
        expectedResult = service.addPlaylistSongJoinToCollectionIfMissing(playlistSongJoinCollection, undefined, null);
        expect(expectedResult).toEqual(playlistSongJoinCollection);
      });
    });

    describe('comparePlaylistSongJoin', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePlaylistSongJoin(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePlaylistSongJoin(entity1, entity2);
        const compareResult2 = service.comparePlaylistSongJoin(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePlaylistSongJoin(entity1, entity2);
        const compareResult2 = service.comparePlaylistSongJoin(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePlaylistSongJoin(entity1, entity2);
        const compareResult2 = service.comparePlaylistSongJoin(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
