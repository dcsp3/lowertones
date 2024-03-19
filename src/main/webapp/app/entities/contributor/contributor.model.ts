import { ISong } from 'app/entities/song/song.model';

export interface IContributor {
  id: number;
  name?: string | null;
  role?: string | null;
  instrument?: string | null;
  musicbrainzID?: string | null;
  songs?: Pick<ISong, 'id'>[] | null;
}

export type NewContributor = Omit<IContributor, 'id'> & { id: null };
