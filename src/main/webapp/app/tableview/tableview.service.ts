import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

interface QueryParams {
  searchQuery: string;
  minDuration: string | null;
  maxDuration: string | null;
  selectedExplicitness: string;
  minPopularity: number | null;
  maxPopularity: number | null;
  artistName: string[];
  minAcousticness: number | null | undefined;
  maxAcousticness: number | null | undefined;
  minDanceability: number | null | undefined;
  maxDanceability: number | null | undefined;
  minInstrumentalness: number | null | undefined;
  maxInstrumentalness: number | null | undefined;
  minEnergy: number | null | undefined;
  maxEnergy: number | null | undefined;
  minLiveness: number | null | undefined;
  maxLiveness: number | null | undefined;
  minLoudness: number | null | undefined;
  maxLoudness: number | null | undefined;
  minSpeechiness: number | null | undefined;
  maxSpeechiness: number | null | undefined;
  minValence: number | null | undefined;
  maxValence: number | null | undefined;
}

@Injectable({
  providedIn: 'root',
})
export class TableviewService {
  private playlistUrl = '/api/tableview-user-playlists';
  private playlistSongsUrl = '/api/tableview-playlist-songs';
  private lowertonesSongsUrl = 'api/tableview-lowertones-songs';

  constructor(private http: HttpClient) {}

  getPlaylists(): Observable<string[]> {
    return this.http.get<string[]>(this.playlistUrl);
  }

  getPlaylistData(playlistId: string): Observable<any> {
    const params = new HttpParams().set('playlistId', playlistId);

    return this.http.get<any>(this.playlistSongsUrl, { params: params });
  }

  getLowertonesData(queryParams: QueryParams): Observable<any> {
    let params = new HttpParams();
    Object.keys(queryParams).forEach(key => {
      const value = queryParams[key as keyof QueryParams];
      if (value !== null && value !== undefined) {
        if (Array.isArray(value)) {
          params = params.set(key, value.join(','));
        } else {
          params = params.set(key, value.toString());
        }
      }
    });

    return this.http.get<any>(this.lowertonesSongsUrl, { params: params });
  }

  exportPlaylist(playlistObject: any): Observable<any> {
    return this.http.post('/api/tableview-export-playlist', playlistObject);
  }
}
