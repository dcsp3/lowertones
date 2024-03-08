import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LinkSpotifyComponent } from './link-spotify.component';

describe('LinkSpotifyComponent', () => {
  let component: LinkSpotifyComponent;
  let fixture: ComponentFixture<LinkSpotifyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LinkSpotifyComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(LinkSpotifyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
