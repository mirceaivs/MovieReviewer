// types.ts
export interface Review {
  id?: number;
  content: string;
  rating: number;
  username?: string; // se poate completa la backend, poate fi ignorat la frontend
  movieId: number;
  movieTitle?: string;
}

