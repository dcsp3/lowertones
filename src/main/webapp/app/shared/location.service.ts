import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class LocationService {
  private currentTabSubject = new BehaviorSubject<string | null>(null);

  constructor() {}

  setCurrentTab(tabId: string) {
    this.currentTabSubject.next(tabId);
  }

  getCurrentTab(): Observable<string | null> {
    return this.currentTabSubject.asObservable();
  }
}
