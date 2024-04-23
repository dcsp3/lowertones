import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TableviewService {
  private playlistUrl = '/api/tableview-user-playlists';
  private playlistDataUrl = '/api/tableview-playlist';

  constructor(private http: HttpClient) {}

  getPlaylists(): Observable<string[]> {
    return this.http.get<string[]>(this.playlistUrl);
  }

  getPlaylistData(playlistId: number): Observable<any> {
    return this.http.get<any>(`${this.playlistDataUrl}/${playlistId}/data`);
  }
}
