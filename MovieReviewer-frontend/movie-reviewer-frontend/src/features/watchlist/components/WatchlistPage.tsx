
import React, { useEffect, useState } from 'react';
import { Navigate, Link } from 'react-router-dom';
import watchlistService from '../services/watchlistService';
import movieService from '../../movies/services/movieService';
import type { Movie } from '../../movies/types';
import type { WatchlistItem } from '../types';

const WatchlistPage: React.FC = () => {
  const [movies, setMovies] = useState<Movie[]>([]);
  const [errorMsg, setErrorMsg] = useState<string | null>(null);
  const [successMsg, setSuccessMsg] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const token = localStorage.getItem('token');

  if (!token) {
    return <Navigate to="/login" replace />;
  }

  const fetchWatchlist = async () => {
    setLoading(true);
    setErrorMsg(null);
    try {
      const res = await watchlistService.getAll();
      const items: WatchlistItem[] = res.data;
      const moviePromises = items.map(item => movieService.getById(item.movieId));
      const moviesResponses = await Promise.all(moviePromises);
      const moviesList = moviesResponses.map(r => r.data);
      setMovies(moviesList);
    } catch (error: any) {
      if (error.response && error.response.status === 401) {
        setErrorMsg('Trebuie sa fiti logat pentru a vedea watchlist-ul.');
      } else {
        setErrorMsg('Eroare la incarcarea watchlist-ului.');
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchWatchlist();
  }, []);

  const handleRemove = async (movieId: number) => {
    setErrorMsg(null);
    setSuccessMsg(null);
    try {
      await watchlistService.removeFromWatchlist(movieId);
      setMovies(prev => prev.filter(m => m.id !== movieId));
      setSuccessMsg('Filmul a fost eliminat din Watchlist.');
    } catch (error: any) {
      if (error.response && error.response.status === 404) {
        setErrorMsg('Filmul nu exista în watchlist.');
      } else if (error.response && error.response.status === 401) {
        setErrorMsg('Trebuie sa fiti autentificat pentru a elimina filme.');
      } else {
        setErrorMsg('Eroare la eliminarea filmului din watchlist.');
      }
    }
  };

  return (
    <div>
      <h2>Watchlist</h2>
      {errorMsg && <div className="alert alert-danger">{errorMsg}</div>}
      {successMsg && <div className="alert alert-success">{successMsg}</div>}
      {loading ? (
        <div>Se incarca...</div>
      ) : movies.length === 0 ? (
        <p><i>Watchlist-ul este gol.</i></p>
      ) : (
        <ul className="list-group">
          {movies.map(movie => (
            <li key={movie.id} className="list-group-item d-flex justify-content-between align-items-center">
              <Link to={`/movies/${movie.id}`}>{movie.title} ({movie.year})</Link>
              <button
                className="btn btn-sm btn-outline-danger"
                onClick={() => handleRemove(movie.id)}
              >
                Sterge
              </button>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default WatchlistPage;
