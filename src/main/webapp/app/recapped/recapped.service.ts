import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RecappedRequest, RecappedDTO } from './models';

@Injectable({
  providedIn: 'root',
})
export class RecappedService {
  private apiUrl = '/api/recapped';
  constructor(private http: HttpClient) {}
  submitRecappedRequest(request: RecappedRequest): Observable<RecappedDTO> {
    return this.http.post<RecappedDTO>(this.apiUrl, request);
  }
}
