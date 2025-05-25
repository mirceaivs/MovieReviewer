
import React, { useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import movieService from "../services/movieService";
import type { OmdbSearchMovie } from "../types";

const MovieList: React.FC = () => {
  const [movies, setMovies] = useState<OmdbSearchMovie[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [totalPages, setTotalPages] = useState(0);

  const [searchParams, setSearchParams] = useSearchParams();
  const [inputValue, setInputValue] = useState(""); 
  const navigate = useNavigate();

  const searchTerm = searchParams.get("search") || "";
  const page = parseInt(searchParams.get("page") || "0", 10);


  useEffect(() => {
    if (!searchTerm) {
      setMovies([]);
      setTotalPages(0);
      setError(null);
      return;
    }
    const searchMoviesOmdb = async () => {
      setLoading(true);
      setError(null);
      try {
        const res = await movieService.searchOmdb(searchTerm, page);
        const results = res.data.Search || res.data.movies || [];
        const total = parseInt(res.data.totalResults, 10) || 0;
        setMovies(
          results.map((m: any) => ({
            imdbID: m.imdbID,
            title: m.Title,
            year: m.Year,
            poster: m.Poster,
          }))
        );
        setTotalPages(Math.ceil(total / 5));
      } catch (e) {
        setError("Eroare la cautare.");
        setMovies([]);
      }
      setLoading(false);
    };
    searchMoviesOmdb();
  }, [searchTerm, page]);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setInputValue(e.target.value);
  };

  const handleSearch = () => {
    setSearchParams({ search: inputValue, page: "0" });
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      handleSearch();
    }
  };

  const handlePrev = () => {
    if (page > 0) {
      setSearchParams({ search: searchTerm, page: String(page - 1) });
    }
  };
  const handleNext = () => {
    if (page < totalPages - 1) {
      setSearchParams({ search: searchTerm, page: String(page + 1) });
    }
  };

  const handleClick = async (imdbID: string) => {
    try {
      const res = await movieService.getByImdb(imdbID);
      const movie = res.data;
      navigate(`/movies/${movie.id}`);
    } catch (e) {
      setError("Eroare la preluarea detaliilor filmului.");
    }
  };

  useEffect(() => {
    setInputValue(searchTerm); 
  }, [searchTerm]);

  return (
    <div>
      <h2>Cautare filme (OMDb)</h2>
      <input
        type="text"
        placeholder="Caută film după titlu..."
        value={inputValue}
        onChange={handleInputChange}
        onKeyDown={handleKeyDown}
        className="form-control mb-2"
      />
      <button className="btn btn-primary mb-3" onClick={handleSearch}>
        Cauta
      </button>
      {loading && <div>Se incarca...</div>}
      {error && <div className="alert alert-danger">{error}</div>}
      <div className="list-group">
        {movies.map(movie => (
          <div key={movie.imdbID} className="list-group-item d-flex align-items-center">
            {movie.poster && (
              <img
                src={movie.poster}
                alt={movie.title}
                style={{ width: 60, height: 90, objectFit: "cover", marginRight: 16 }}
              />
            )}
            <div>
              <button
                className="btn btn-link fw-bold p-0 text-start"
                onClick={() => handleClick(movie.imdbID)}
              >
                {movie.title}
              </button>
              <span className="text-muted ms-2">{movie.year}</span>
            </div>
          </div>
        ))}
      </div>
      <div className="mt-3">
        {page > 0 && (
          <button className="btn btn-outline-secondary me-2" onClick={handlePrev}>
            &laquo; Anterioara
          </button>
        )}
        {page < totalPages - 1 && (
          <button className="btn btn-outline-secondary" onClick={handleNext}>
            Urmatoare &raquo;
          </button>
        )}
      </div>
    </div>
  );
};

export default MovieList;
