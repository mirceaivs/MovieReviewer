import API from '../../../axiosClient';
import type { WatchlistItem } from '../types';

const watchlistService = {
  getAll: () => {
    return API.get<WatchlistItem[]>('/watchlist');
  },
  addToWatchlist: (movieId: number) => {
    return API.post<WatchlistItem>(`/watchlist/${movieId}`);
  },
  removeFromWatchlist: (movieId: number) => {
    return API.delete<void>(`/watchlist/${movieId}`);
  }
};

export default watchlistService;
