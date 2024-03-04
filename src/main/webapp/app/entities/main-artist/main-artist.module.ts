import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MainArtistComponent } from './list/main-artist.component';
import { MainArtistDetailComponent } from './detail/main-artist-detail.component';
import { MainArtistUpdateComponent } from './update/main-artist-update.component';
import { MainArtistDeleteDialogComponent } from './delete/main-artist-delete-dialog.component';
import { MainArtistRoutingModule } from './route/main-artist-routing.module';

@NgModule({
  imports: [SharedModule, MainArtistRoutingModule],
  declarations: [MainArtistComponent, MainArtistDetailComponent, MainArtistUpdateComponent, MainArtistDeleteDialogComponent],
})
export class MainArtistModule {}
