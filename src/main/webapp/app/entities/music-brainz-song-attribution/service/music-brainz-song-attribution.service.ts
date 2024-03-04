import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMusicBrainzSongAttribution, NewMusicBrainzSongAttribution } from '../music-brainz-song-attribution.model';

export type PartialUpdateMusicBrainzSongAttribution = Partial<IMusicBrainzSongAttribution> & Pick<IMusicBrainzSongAttribution, 'id'>;

export type EntityResponseType = HttpResponse<IMusicBrainzSongAttribution>;
export type EntityArrayResponseType = HttpResponse<IMusicBrainzSongAttribution[]>;

@Injectable({ providedIn: 'root' })
export class MusicBrainzSongAttributionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/music-brainz-song-attributions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(musicBrainzSongAttribution: NewMusicBrainzSongAttribution): Observable<EntityResponseType> {
    return this.http.post<IMusicBrainzSongAttribution>(this.resourceUrl, musicBrainzSongAttribution, { observe: 'response' });
  }

  update(musicBrainzSongAttribution: IMusicBrainzSongAttribution): Observable<EntityResponseType> {
    return this.http.put<IMusicBrainzSongAttribution>(
      `${this.resourceUrl}/${this.getMusicBrainzSongAttributionIdentifier(musicBrainzSongAttribution)}`,
      musicBrainzSongAttribution,
      { observe: 'response' }
    );
  }

  partialUpdate(musicBrainzSongAttribution: PartialUpdateMusicBrainzSongAttribution): Observable<EntityResponseType> {
    return this.http.patch<IMusicBrainzSongAttribution>(
      `${this.resourceUrl}/${this.getMusicBrainzSongAttributionIdentifier(musicBrainzSongAttribution)}`,
      musicBrainzSongAttribution,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMusicBrainzSongAttribution>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMusicBrainzSongAttribution[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMusicBrainzSongAttributionIdentifier(musicBrainzSongAttribution: Pick<IMusicBrainzSongAttribution, 'id'>): number {
    return musicBrainzSongAttribution.id;
  }

  compareMusicBrainzSongAttribution(
    o1: Pick<IMusicBrainzSongAttribution, 'id'> | null,
    o2: Pick<IMusicBrainzSongAttribution, 'id'> | null
  ): boolean {
    return o1 && o2 ? this.getMusicBrainzSongAttributionIdentifier(o1) === this.getMusicBrainzSongAttributionIdentifier(o2) : o1 === o2;
  }

  addMusicBrainzSongAttributionToCollectionIfMissing<Type extends Pick<IMusicBrainzSongAttribution, 'id'>>(
    musicBrainzSongAttributionCollection: Type[],
    ...musicBrainzSongAttributionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const musicBrainzSongAttributions: Type[] = musicBrainzSongAttributionsToCheck.filter(isPresent);
    if (musicBrainzSongAttributions.length > 0) {
      const musicBrainzSongAttributionCollectionIdentifiers = musicBrainzSongAttributionCollection.map(
        musicBrainzSongAttributionItem => this.getMusicBrainzSongAttributionIdentifier(musicBrainzSongAttributionItem)!
      );
      const musicBrainzSongAttributionsToAdd = musicBrainzSongAttributions.filter(musicBrainzSongAttributionItem => {
        const musicBrainzSongAttributionIdentifier = this.getMusicBrainzSongAttributionIdentifier(musicBrainzSongAttributionItem);
        if (musicBrainzSongAttributionCollectionIdentifiers.includes(musicBrainzSongAttributionIdentifier)) {
          return false;
        }
        musicBrainzSongAttributionCollectionIdentifiers.push(musicBrainzSongAttributionIdentifier);
        return true;
      });
      return [...musicBrainzSongAttributionsToAdd, ...musicBrainzSongAttributionCollection];
    }
    return musicBrainzSongAttributionCollection;
  }
}
