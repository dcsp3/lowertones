import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecappedComponent } from './recapped.component';

describe('RecappedComponent', () => {
  let component: RecappedComponent;
  let fixture: ComponentFixture<RecappedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RecappedComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(RecappedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
