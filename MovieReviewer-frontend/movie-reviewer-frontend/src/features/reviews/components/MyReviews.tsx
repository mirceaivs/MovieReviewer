import React, { useEffect, useState } from 'react';
import { useAuth } from '../../../context/AuthContext';
import type { Review } from '../types';
import reviewService from '../services/reviewService';
import { Link } from 'react-router-dom';

const MyReviews: React.FC = () => {
  const [reviews, setReviews] = useState<Review[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const { token } = useAuth();

  useEffect(() => {
    if (!token) return;
    setLoading(true);
    reviewService.getMyReviews()
      .then(res => setReviews(res.data))
      .catch(() => setError('Eroare la încărcarea recenziilor.'))
      .finally(() => setLoading(false));
  }, [token]);

  if (!token) return <div className="alert alert-warning mt-4">Trebuie sa fii logat!</div>;

  return (
    <div className="container mt-4">
      <h2>Review-urile mele</h2>
      {loading && <div>Se incarca...</div>}
      {error && <div className="alert alert-danger">{error}</div>}
      {reviews.length === 0 && !loading && <div>Nu ai niciun review momentan.</div>}
      <ul className="list-group mt-3">
        {reviews.map((rev) => (
          <li className="list-group-item" key={rev.id}>
          <b>Filmul:</b>{" "}
            {rev.movieTitle ? (
                <Link to={`/movies/${rev.movieId}`}>{rev.movieTitle}</Link>
                ) : (
                <span className="text-muted">(necunoscut)</span>
            )}<br />


            <b>Rating:</b> {rev.rating} <br />
            <b>Comentariu:</b> {rev.content}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default MyReviews;
