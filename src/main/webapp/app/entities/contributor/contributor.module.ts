import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContributorComponent } from './list/contributor.component';
import { ContributorDetailComponent } from './detail/contributor-detail.component';
import { ContributorUpdateComponent } from './update/contributor-update.component';
import { ContributorDeleteDialogComponent } from './delete/contributor-delete-dialog.component';
import { ContributorRoutingModule } from './route/contributor-routing.module';

@NgModule({
  imports: [SharedModule, ContributorRoutingModule],
  declarations: [ContributorComponent, ContributorDetailComponent, ContributorUpdateComponent, ContributorDeleteDialogComponent],
})
export class ContributorModule {}
