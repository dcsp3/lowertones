import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DataMethodologyComponent } from './data-methodology.component';

describe('DataMethodologyComponent', () => {
  let component: DataMethodologyComponent;
  let fixture: ComponentFixture<DataMethodologyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DataMethodologyComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(DataMethodologyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
