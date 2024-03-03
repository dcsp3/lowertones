import { ISong } from 'app/entities/song/song.model';

export interface IContributor {
  id: number;
  name?: string | null;
  role?: string | null;
  musicbrainzID?: string | null;
  song?: Pick<ISong, 'id'> | null;
}

export type NewContributor = Omit<IContributor, 'id'> & { id: null };
