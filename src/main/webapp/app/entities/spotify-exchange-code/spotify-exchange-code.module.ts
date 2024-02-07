import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SpotifyExchangeCodeComponent } from './list/spotify-exchange-code.component';
import { SpotifyExchangeCodeDetailComponent } from './detail/spotify-exchange-code-detail.component';
import { SpotifyExchangeCodeUpdateComponent } from './update/spotify-exchange-code-update.component';
import { SpotifyExchangeCodeDeleteDialogComponent } from './delete/spotify-exchange-code-delete-dialog.component';
import { SpotifyExchangeCodeRoutingModule } from './route/spotify-exchange-code-routing.module';

@NgModule({
  imports: [SharedModule, SpotifyExchangeCodeRoutingModule],
  declarations: [
    SpotifyExchangeCodeComponent,
    SpotifyExchangeCodeDetailComponent,
    SpotifyExchangeCodeUpdateComponent,
    SpotifyExchangeCodeDeleteDialogComponent,
  ],
})
export class SpotifyExchangeCodeModule {}
