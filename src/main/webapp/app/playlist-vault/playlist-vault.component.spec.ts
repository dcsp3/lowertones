import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlaylistVaultComponent } from './playlist-vault.component';

describe('PlaylistVaultComponent', () => {
  let component: PlaylistVaultComponent;
  let fixture: ComponentFixture<PlaylistVaultComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlaylistVaultComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PlaylistVaultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
