// import { useState, useEffect } from 'react';
// import movieService from '../../movies/services/movieService';
// import type { Movie } from '../../movies/types';


// export function useMovieSearch(
//   searchTerm: string,
//   genres: string[],
//   actors: string[],
//   page: number,
//   isAuthenticated: boolean
// ) {
//   const [movies, setMovies] = useState<Movie[]>([]);
//   const [loading, setLoading] = useState(false);
//   const [error, setError] = useState<string | null>(null);
//   const [totalPages, setTotalPages] = useState(0);

//   useEffect(() => {
//     if (!isAuthenticated) return;
//     setLoading(true);
//     setError(null);
//     movieService.searchMovies(searchTerm, genres, actors, page)
//       .then(res => {
//         setMovies(res.data.content);
//         setTotalPages(res.data.totalPages);
//       })
//       .catch(() => setError('Eroare la încărcarea filmelor.'))
//       .finally(() => setLoading(false));
//   }, [searchTerm, genres, actors, page, isAuthenticated]);

//   return { movies, loading, error, totalPages };
// }
