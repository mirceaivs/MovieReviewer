
import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth, useIsAdmin } from '../../../context/AuthContext';
import movieService from '../services/movieService';
import watchlistService from '../../watchlist/services/watchlistService';
import reviewService from '../../reviews/services/reviewService';
import type { Movie } from '../types';
import type { Review } from '../../reviews/types';
import ReviewList from '../../reviews/components/ReviewList';
import AddReviewForm from '../../reviews/components/AddReviewForm';

const MovieDetails: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const movieId = id ? parseInt(id) : null;
  const [movie, setMovie] = useState<Movie | null>(null);
  const [reviews, setReviews] = useState<Review[]>([]);
  const [errorMsg, setErrorMsg] = useState<string | null>(null);
  const [successMsg, setSuccessMsg] = useState<string | null>(null);
  const [reviewToEdit, setReviewToEdit] = useState<Review | null>(null);
  const [isInWatchlist, setIsInWatchlist] = useState<boolean>(false);
  const { user, token } = useAuth();
  const isAdmin = useIsAdmin();
  const navigate = useNavigate();

  useEffect(() => {
    const loadAll = async () => {
      try {
        if (!movieId) return;
        const [movieRes, reviewsRes, watchlistRes] = await Promise.all([
          movieService.getById(movieId),
          reviewService.getByMovie(movieId),
          token ? watchlistService.getAll() : Promise.resolve({ data: [] }),
        ]);
        setMovie(movieRes.data);
        setReviews(reviewsRes.data);

        if (token && watchlistRes.data) {
          setIsInWatchlist(
            watchlistRes.data.some((item: any) => item.movieId === movieId)
          );
        } else {
          setIsInWatchlist(false);
        }
      } catch (error: any) {
        setErrorMsg('Eroare la incarcarea datelor filmului.');
      }
    };
    loadAll();
  }, [movieId, token, successMsg]);

  const handleToggleWatchlist = async () => {
    if (!movieId) return;
    if (isInWatchlist) {
      try {
        await watchlistService.removeFromWatchlist(movieId);
        setIsInWatchlist(false);
        setSuccessMsg('Filmul a fost sters din Watchlist.');
      } catch (error: any) {
        setErrorMsg(error.response?.data?.error || 'Eroare la eliminare.');
      }
    } else {
      try {
        await watchlistService.addToWatchlist(movieId);
        setIsInWatchlist(true);
        setSuccessMsg('Filmul a fost adaugat la Watchlist.');
      } catch (error: any) {
        setErrorMsg(error.response?.data?.error || 'Eroare la adaugare.');
      }
    }
  };

  const handleDeleteMovie = async () => {
    if (!movieId) return;
    if (!window.confirm('Est sigur că vrei sa stergi filmul?!')) return;
    try {
      await movieService.deleteMovie(movieId);
      navigate('/movies'); 
    } catch (error: any) {
      setErrorMsg('Eroare la stergerea filmului!');
    }
  };

 const handleEditMovie = () => {
  if (!movieId) return;
  navigate(`/movies/${movieId}/edit`);
};


  const alreadyReviewed = user && reviews.some((r) => r.username === user.username);

  const handleReviewSaved = (savedReview: Review, mode: "add" | "edit") => {
    if (mode === "add") {
      setReviews(prev => [...prev, savedReview]);
    } else {
      setReviews(prev => prev.map(r => r.id === savedReview.id ? savedReview : r));
      setReviewToEdit(null);
    }
  };

  
  const handleDeleteReview = (id: number) => {
    if (window.confirm("Esti sigur ca vrei sa stergi recenzia?")) {
      reviewService.deleteReview(movieId!, id)
        .then(() => setReviews(reviews.filter(r => r.id !== id)))
        .catch(() => setErrorMsg("Eroare la stergerea recenziei!"));
    }
  };

  const handleEditReview = (review: Review) => setReviewToEdit(review);

  return (
    <div>
      {errorMsg && <div className="alert alert-danger">{errorMsg}</div>}
      {successMsg && <div className="alert alert-success">{successMsg}</div>}
      {movie ? (
        <>
          <div className="d-flex justify-content-between align-items-center">
            <h3>{movie.title} ({movie.year})</h3>
            {isAdmin && (
              <div>
                <button className="btn btn-outline-warning me-2" onClick={handleEditMovie}>
                  Editeaza film
                </button>
                <button className="btn btn-outline-danger" onClick={handleDeleteMovie}>
                  Stergere film
                </button>
              </div>
            )}
          </div>
          {movie.poster && (
            <img src={movie.poster} alt={movie.title} className="img-fluid mb-3" style={{ maxWidth: '200px' }} />
          )}
          <p><strong>Genuri:</strong> {movie.genres.join(', ')}</p>
          <p><strong>Regizor:</strong> {movie.director}</p>
          <p><strong>Actori:</strong> {movie.actors.join(', ')}</p>
          <p><strong>Descriere:</strong> {movie.plot}</p>

          {token && (
            <button
              className={`btn mb-3 ${isInWatchlist ? 'btn-danger' : 'btn-outline-primary'}`}
              onClick={handleToggleWatchlist}
            >
              {isInWatchlist ? 'Șterge din Watchlist' : 'Adaugă la Watchlist'}
            </button>
          )}

          <h5>Recenzii:</h5>
          <ReviewList
            reviews={reviews}
            onEdit={handleEditReview}
            onDelete={handleDeleteReview}
            isAdmin={isAdmin} 
            currentUser={user?.username}
          />

          {token ? (
            (!alreadyReviewed || reviewToEdit) ? (
              <AddReviewForm
                movieId={movieId!}
                onReviewSaved={handleReviewSaved}
                reviewToEdit={reviewToEdit}
                onCancelEdit={() => setReviewToEdit(null)}
              />
            ) : (
              <p className="alert alert-info">
                Ai adaugat deja o recenzie pentru acest film. O poti edita sau sterge.
              </p>
            )
          ) : (
            <p>
              <i>Logati-va pentru a scrie o recenzie.</i>
            </p>
          )}
        </>
      ) : (
        <p>Se incarca detaliile filmului...</p>
      )}
    </div>
  );
};

export default MovieDetails;
