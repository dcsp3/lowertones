export interface ISpotifyExchangeCode {
  id: number;
}

export type NewSpotifyExchangeCode = Omit<ISpotifyExchangeCode, 'id'> & { id: null };
