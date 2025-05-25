import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import movieService from "../services/movieService";
import genreService from "../../genres/services/genreService";
import type { Movie } from "../types";
import type { Genre } from "../../genres/types";

const EditMoviePage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [movie, setMovie] = useState<Movie | null>(null);
  const [genres, setGenres] = useState<Genre[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(false);

  useEffect(() => {
    if (!id) return;
    setLoading(true);
    Promise.all([
      movieService.getById(Number(id)),
      genreService.getAll(),
    ])
      .then(([movieRes, genresRes]) => {
        setMovie(movieRes.data);
        setGenres(genresRes.data);
      })
      .catch(() => setError("Eroare la incarcarea datelor!"))
      .finally(() => setLoading(false));
  }, [id]);

  
  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    if (!movie) return;
    setMovie({ ...movie, [e.target.name]: e.target.value });
  };

  
  const handleGenreToggle = (genreName: string) => {
    if (!movie) return;
    setMovie((prev) =>
      prev
        ? {
            ...prev,
            genres: prev.genres.includes(genreName)
              ? prev.genres.filter((g) => g !== genreName)
              : [...prev.genres, genreName],
          }
        : prev
    );
  };

 const handleSubmit = async (e: React.FormEvent) => {
  e.preventDefault();
  if (!movie) return;
  
  const updatePayload = {
    title: movie.title,
    year: movie.year,
    plot: movie.plot,
    genres: movie.genres
  };
  try {
    await movieService.updateMovie(movie.id, updatePayload);
    navigate(`/movies/${movie.id}`);
  } catch {
    setError("Eroare la salvarea!");
  }
};


  if (loading) return <p>Se incarca datele...</p>;
  if (!movie) return <p>Nu s-au gasit date pentru acest film.</p>;

  return (
    <div className="container my-4">
      <h2>Editeaza filmul</h2>
      {error && <div className="alert alert-danger">{error}</div>}
      <div className="mb-3">
        <strong>Regizor:</strong>{" "}
        <span>{movie.director || <i>(necunoscut)</i>}</span>
      </div>
      <div className="mb-3">
        <strong>Actori:</strong>{" "}
        <span>
          {movie.actors && movie.actors.length > 0
            ? movie.actors.join(", ")
            : <i>(niciun actor introdus)</i>}
        </span>
      </div>
      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label className="form-label">Titlu</label>
          <input
            className="form-control"
            name="title"
            value={movie.title}
            onChange={handleChange}
            required
          />
        </div>
        <div className="mb-3">
          <label className="form-label">An</label>
          <input
            className="form-control"
            name="year"
            value={movie.year}
            onChange={handleChange}
            required
          />
        </div>
        <div className="mb-3">
          <label className="form-label">Descriere</label>
          <textarea
            className="form-control"
            name="plot"
            value={movie.plot}
            onChange={handleChange}
            rows={3}
          />
        </div>
        <div className="mb-3">
          <label className="form-label"><strong>Genuri:</strong></label>
          <div>
            {genres.map((g) => (
              <label key={g.id} className="me-3">
                <input
                  type="checkbox"
                  value={g.name}
                  checked={movie.genres.includes(g.name)}
                  onChange={() => handleGenreToggle(g.name)}
                />{" "}
                {g.name}
              </label>
            ))}
          </div>
        </div>
        <button className="btn btn-primary" type="submit">
          Salveaza modificarile
        </button>
      </form>
    </div>
  );
};

export default EditMoviePage;
