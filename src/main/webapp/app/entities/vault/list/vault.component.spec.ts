import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { VaultService } from '../service/vault.service';

import { VaultComponent } from './vault.component';

describe('Vault Management Component', () => {
  let comp: VaultComponent;
  let fixture: ComponentFixture<VaultComponent>;
  let service: VaultService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'vault', component: VaultComponent }]), HttpClientTestingModule],
      declarations: [VaultComponent],
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
      .overrideTemplate(VaultComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VaultComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(VaultService);

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
    expect(comp.vaults?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to vaultService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getVaultIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getVaultIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
