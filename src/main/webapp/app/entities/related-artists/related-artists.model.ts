export interface IRelatedArtists {
  id: number;
  mainArtistSptfyID?: string | null;
  relatedArtistSpotifyID1?: string | null;
  relatedArtistSpotifyID2?: string | null;
  relatedArtistSpotifyID3?: string | null;
  relatedArtistSpotifyID4?: string | null;
  relatedArtistSpotifyID5?: string | null;
  relatedArtistSpotifyID6?: string | null;
  relatedArtistSpotifyID7?: string | null;
  relatedArtistSpotifyID8?: string | null;
  relatedArtistSpotifyID9?: string | null;
  relatedArtistSpotifyID10?: string | null;
  relatedArtistSpotifyID11?: string | null;
  relatedArtistSpotifyID12?: string | null;
  relatedArtistSpotifyID13?: string | null;
  relatedArtistSpotifyID14?: string | null;
  relatedArtistSpotifyID15?: string | null;
  relatedArtistSpotifyID16?: string | null;
  relatedArtistSpotifyID17?: string | null;
  relatedArtistSpotifyID18?: string | null;
  relatedArtistSpotifyID19?: string | null;
  relatedArtistSpotifyID20?: string | null;
}

export type NewRelatedArtists = Omit<IRelatedArtists, 'id'> & { id: null };
