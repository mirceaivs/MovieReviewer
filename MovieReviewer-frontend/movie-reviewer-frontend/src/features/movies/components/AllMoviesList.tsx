
import React, { useState, useEffect } from 'react';
import { Link} from 'react-router-dom';
import movieService from '../services/movieService';
import genreService from '../../genres/services/genreService';
import type { Movie } from '../types';
import type { Genre } from '../../genres/types';

const AllMoviesList: React.FC = () => {
  const [movies, setMovies] = useState<Movie[]>([]);
  const [page, setPage] = useState<number>(0);
  const [size] = useState<number>(6);
  const [sortParam, setSortParam] = useState<string>('title,asc');
  const [selectedGenres, setSelectedGenres] = useState<string[]>([]);
  const [totalPages, setTotalPages] = useState<number>(0);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string|null>(null);
  const [genres, setGenres] = useState<Genre[]>([]);

 

  useEffect(() => {
    genreService.getAll()
      .then(res => setGenres(res.data))
      .catch(() => setError('Eroare la incarcare genurilor!'));
  }, []);

  useEffect(() => {
    const fetchMovies = async () => {
      setLoading(true);
      setError(null);
      try {
        const params: any = { page, size, sort: sortParam };
        if (selectedGenres.length > 0) {
          params.genres = selectedGenres.join(',');
        }
        const res = await movieService.getMovies(params);
        setMovies(res.data.content);
        setTotalPages(res.data.totalPages);
      } catch (err) {
        setError("Eroare la incarcarea filmelor!");
      } finally {
        setLoading(false);
      }
    };
    fetchMovies();
  }, [page, size, sortParam, selectedGenres]);

  const toggleGenre = (genre: string) => {
    setSelectedGenres(prev =>
      prev.includes(genre) ? prev.filter(g => g !== genre) : [...prev, genre]
    );
    setPage(0);
  };

  return (
    <div className="container my-4">
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h2>Toate Filmele</h2>
      </div>

      <div className="row mb-3">
        <div className="col-md-4">
          <label><strong>Genuri:</strong></label>
          {genres.map(g => (
            <div key={g.id}>
              <label>
                <input
                  type="checkbox"
                  value={g.name}
                  checked={selectedGenres.includes(g.name)}
                  onChange={() => toggleGenre(g.name)}
                  className="me-1"
                />
                {g.name}
              </label>
            </div>
          ))}
        </div>
        <div className="col-md-4">
          <label><strong>Sortare:</strong></label>
          <select
            className="form-select"
            value={sortParam}
            onChange={e => { setSortParam(e.target.value); setPage(0); }}
          >
            <option value="title,asc">Titlu A–Z</option>
            <option value="title,desc">Titlu Z–A</option>
            <option value="imdbRating,asc">IMDb Rating ↑</option>
            <option value="imdbRating,desc">IMDb Rating ↓</option>
          </select>
        </div>
      </div>

      {error && <div className="alert alert-danger">{error}</div>}
      {loading && <div className="text-center"><span className="spinner-border"></span> Se incarca...</div>}

      <div className="row">
        {movies.map(movie => (
          <div key={movie.id} className="col-md-6 mb-4">
            <div className="card h-100">
              <div className="card-body d-flex">
                {movie.poster && (
                  <img src={movie.poster} alt={movie.title}
                       style={{ width: 90, height: 130, objectFit: "cover", marginRight: 16, borderRadius: 8 }} />
                )}
                <div className="flex-grow-1">
                  <h5 className="card-title">
                    <Link to={`/movies/${movie.id}`} className="fw-bold text-decoration-none">
                      {movie.title}
                    </Link>
                    <span className="text-muted"> ({movie.year})</span>
                  </h5>
                  {movie.genres && movie.genres.length > 0 && (
                    <div><strong>Genuri:</strong> {movie.genres.join(", ")}</div>
                  )}
                  {movie.director && <div><strong>Regizor:</strong> {movie.director}</div>}
                  {movie.actors && movie.actors.length > 0 && (
                    <div><strong>Actori:</strong> {movie.actors.join(", ")}</div>
                  )}
                  {movie.imdbRating && <div><strong>IMDb Rating:</strong> {movie.imdbRating}</div>}
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>

      <div className="d-flex justify-content-center">
        <button
          className="btn btn-outline-secondary me-2"
          disabled={page <= 0}
          onClick={() => setPage(prev => prev - 1)}
        >
          &laquo; Anterior
        </button>
        <button
          className="btn btn-outline-secondary"
          disabled={page >= totalPages - 1}
          onClick={() => setPage(prev => prev + 1)}
        >
          Urmator &raquo;
        </button>
      </div>
    </div>
  );
};

export default AllMoviesList;
