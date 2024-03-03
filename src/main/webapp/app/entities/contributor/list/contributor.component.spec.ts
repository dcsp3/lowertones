import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ContributorService } from '../service/contributor.service';

import { ContributorComponent } from './contributor.component';

describe('Contributor Management Component', () => {
  let comp: ContributorComponent;
  let fixture: ComponentFixture<ContributorComponent>;
  let service: ContributorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'contributor', component: ContributorComponent }]), HttpClientTestingModule],
      declarations: [ContributorComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(ContributorComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContributorComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ContributorService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.contributors?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to contributorService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getContributorIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getContributorIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
