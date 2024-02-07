import { ISpotifyExchangeCode, NewSpotifyExchangeCode } from './spotify-exchange-code.model';

export const sampleWithRequiredData: ISpotifyExchangeCode = {
  id: 8600,
};

export const sampleWithPartialData: ISpotifyExchangeCode = {
  id: 98483,
};

export const sampleWithFullData: ISpotifyExchangeCode = {
  id: 90974,
};

export const sampleWithNewData: NewSpotifyExchangeCode = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
