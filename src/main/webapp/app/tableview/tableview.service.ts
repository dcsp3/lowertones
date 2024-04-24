import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TableviewService {
  private playlistUrl = '/api/tableview-user-playlists';
  private playlistSongsUrl = '/api/tableview-playlist-songs';

  constructor(private http: HttpClient) {}

  getPlaylists(): Observable<string[]> {
    return this.http.get<string[]>(this.playlistUrl);
  }

  getPlaylistData(playlistId: string): Observable<any> {
    const params = new HttpParams().set('playlistId', playlistId);

    return this.http.get<any>(this.playlistSongsUrl, { params: params });
  }
}
