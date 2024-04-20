import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class NetworkService {
  private playlistUrl = '/api/user-playlists';

  constructor(private http: HttpClient) {}

  getPlaylists(): Observable<string[]> {
    return this.http.get<string[]>(this.playlistUrl);
  }
}
