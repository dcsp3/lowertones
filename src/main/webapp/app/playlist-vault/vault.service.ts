import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

import { VaultRequest } from './models';

@Injectable({
  providedIn: 'root',
})
export class VaultService {
  private apiUrl = `/api/vaults`;

  constructor(private http: HttpClient) {}

  getVaults(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  getVaultsByUserId(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/user`);
  }

  createVault(vaultData: VaultRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/create-task`, vaultData);
  }

  deleteVault(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
