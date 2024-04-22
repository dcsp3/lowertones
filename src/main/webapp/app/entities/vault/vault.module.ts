import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VaultComponent } from './list/vault.component';
import { VaultDetailComponent } from './detail/vault-detail.component';
import { VaultUpdateComponent } from './update/vault-update.component';
import { VaultDeleteDialogComponent } from './delete/vault-delete-dialog.component';
import { VaultRoutingModule } from './route/vault-routing.module';

@NgModule({
  imports: [SharedModule, VaultRoutingModule],
  declarations: [VaultComponent, VaultDetailComponent, VaultUpdateComponent, VaultDeleteDialogComponent],
})
export class VaultModule {}
