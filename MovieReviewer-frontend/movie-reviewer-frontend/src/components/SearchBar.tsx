import React, { useState, useEffect } from "react";

interface SearchBarProps {
  onSearch: (title: string, genres: string[], actors: string[]) => void;
  loading?: boolean;
}

const SearchBar: React.FC<SearchBarProps> = ({ onSearch, loading }) => {
  const [title, setTitle] = useState("");
  const [useGenre, setUseGenre] = useState(false);
  const [useActor, setUseActor] = useState(false);

  const [allGenres, setAllGenres] = useState<string[]>([]);
  const [selectedGenre, setSelectedGenre] = useState<string>("");

  const [actor, setActor] = useState("");

  useEffect(() => {
    if (useGenre) {
      fetch("/genres/all")
        .then(res => res.json())
        .then(data => setAllGenres(data))
        .catch(() => setAllGenres([]));
    }
  }, [useGenre]);

  const handleCheckbox = (type: "genre" | "actor") => {
    if (type === "genre") {
      setUseGenre(true);
      setUseActor(false);
    } else {
      setUseGenre(false);
      setUseActor(true);
    }
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (useGenre && selectedGenre) {
      onSearch("", [selectedGenre], []);
    } else if (useActor && actor) {
      onSearch("", [], [actor]);
    } else if (!useGenre && !useActor && title) {
      onSearch(title, [], []);
    }
  };

  return (
    <form className="border rounded p-3 shadow-sm bg-white mb-3" onSubmit={handleSubmit}>
      <div className="row g-2 align-items-center">
        {!useGenre && !useActor && (
          <div className="col-md-6">
            <input
              className="form-control"
              type="text"
              placeholder="Caută filme după titlu..."
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              disabled={useGenre || useActor}
            />
          </div>
        )}

        <div className="col-auto d-flex align-items-center">
          <input
            type="checkbox"
            id="genre-check"
            checked={useGenre}
            onChange={() => handleCheckbox("genre")}
            disabled={useActor}
          />
          <label htmlFor="genre-check" className="ms-2 mb-0">
            Cauta dupa gen
          </label>
        </div>
        {useGenre && (
          <div className="col-md-4">
            <select
              className="form-select"
              value={selectedGenre}
              onChange={e => setSelectedGenre(e.target.value)}
            >
              <option value="">Selecteaza un gen</option>
              {allGenres.map(genre => (
                <option key={genre} value={genre}>
                  {genre}
                </option>
              ))}
            </select>
          </div>
        )}

        <div className="col-auto d-flex align-items-center">
          <input
            type="checkbox"
            id="actor-check"
            checked={useActor}
            onChange={() => handleCheckbox("actor")}
            disabled={useGenre}
          />
          <label htmlFor="actor-check" className="ms-2 mb-0">
            Cauta dupa actor
          </label>
        </div>
        {useActor && (
          <div className="col-md-4">
            <input
              className="form-control"
              type="text"
              placeholder="Introdu nume actor"
              value={actor}
              onChange={e => setActor(e.target.value)}
            />
          </div>
        )}

        <div className="col-md-2">
          <button className="btn btn-primary w-100" type="submit" disabled={loading}>
            Cauta
          </button>
        </div>
      </div>
    </form>
  );
};

export default SearchBar;
