import axios from 'axios';

const getTopArtists = async (timeRange: string, accessToken: string, refreshToken: string): Promise<[number, string][]> => {
  const response = await axios.get(`https://api.spotify.com/v1/me/top/artists?offset=0&limit=15&time_range=${timeRange}`, {
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${accessToken}`,
    },
  });

  const data = response.data;

  const result: [number, string][] = [];
  const minDistance = 50;
  const maxDistance = 300;

  for (let count = 1; count <= data.items.length; count++) {
    const distance = minDistance + ((maxDistance - minDistance) / data.items.length) * count;
    const artistInfo: [number, string] = [distance, data.items[count - 1].name];
    result.push(artistInfo);
  }

  return result;
};

export { getTopArtists };
