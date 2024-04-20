import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IAppUser } from 'app/entities/app-user/app-user.model';

@Injectable({
  providedIn: 'root',
})
export class PreferencesService {
  constructor(private http: HttpClient) {}

  getAppUser(): Observable<IAppUser> {
    return this.http.post<IAppUser>('/api/account/preferences', {});
  }
}
