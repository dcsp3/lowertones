import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root',
})
export class IsLinkedService {
  constructor(private http: HttpClient) {}

  isLinked(): Observable<boolean> {
    return this.http.get<boolean>('/api/is-spotify-linked');
  }
}
