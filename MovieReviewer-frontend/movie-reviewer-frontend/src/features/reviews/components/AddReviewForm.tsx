
import React, { useEffect, useState } from 'react';
import reviewService from '../services/reviewService';
import type { Review } from '../types';

interface AddReviewFormProps {
  movieId: number;
  onReviewSaved: (review: Review, mode: "add" | "edit") => void;
  reviewToEdit?: Review | null;
  onCancelEdit?: () => void;
}

const AddReviewForm: React.FC<AddReviewFormProps> = ({
  movieId,
  onReviewSaved,
  reviewToEdit,
  onCancelEdit
}) => {
  const [content, setContent] = useState('');
  const [rating, setRating] = useState<number | ''>('');
  const [fieldErrors, setFieldErrors] = useState<{ [key: string]: string }>({});
  const [errorMsg, setErrorMsg] = useState<string | null>(null);
  const [successMsg, setSuccessMsg] = useState<string | null>(null);

  useEffect(() => {
    if (reviewToEdit) {
      setContent(reviewToEdit.content);
      setRating(reviewToEdit.rating);
      setSuccessMsg(null);
      setErrorMsg(null);
      setFieldErrors({});
    } else {
      setContent('');
      setRating('');
    }
  }, [reviewToEdit]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setFieldErrors({});
    setErrorMsg(null);
    setSuccessMsg(null);
    try {
      if (reviewToEdit && reviewToEdit.id) {
        const response = await reviewService.updateReview(
          movieId,
          reviewToEdit.id,
          { content, rating: Number(rating) }
        );
        const updatedReview = response.data;
        onReviewSaved(updatedReview, "edit");
        setSuccessMsg('Recenzia a fost editata cu succes.');
      } else {
        const response = await reviewService.addReview({
          movieId,
          content,
          rating: Number(rating),
        });
        const createdReview = response.data;
        onReviewSaved(createdReview, "add");
        setSuccessMsg('Recenzia a fost adaugata cu succes.');
        setContent('');
        setRating('');
      }
    } catch (error: any) {
      if (error.response) {
        const data = error.response.data;
        if (data && (data.error || typeof data === 'string')) {
          setErrorMsg(data.error || data);
        } else if (error.response.status === 401) {
          setErrorMsg('Trebuie sa fiti logat pentru a trimite o recenzie.');
        } else {
          setErrorMsg('Eroare la trimiterea recenziei.');
        }
      } else {
        setErrorMsg('Eroare la trimiterea recenziei.');
      }
    }
  };

  return (
    <div className="card p-3 mb-3">
      <h5>{reviewToEdit ? 'Editeaza recenzia' : 'Adauga o recenzie'}</h5>
      {errorMsg && <div className="alert alert-danger">{errorMsg}</div>}
      {successMsg && <div className="alert alert-success">{successMsg}</div>}
      <form onSubmit={handleSubmit}>
        <div className="mb-2">
          <label className="form-label">Continut</label>
          <textarea
            className={`form-control ${fieldErrors.content ? 'is-invalid' : ''}`}
            value={content}
            onChange={e => setContent(e.target.value)}
            rows={3}
            required
          />
          {fieldErrors.content && <div className="invalid-feedback">{fieldErrors.content}</div>}
        </div>
        <div className="mb-2">
          <label className="form-label">Rating</label>
          <select
            className={`form-select ${fieldErrors.rating ? 'is-invalid' : ''}`}
            value={rating}
            onChange={e => setRating(e.target.value ? Number(e.target.value) : '')}
            required
          >
            <option value="">Selecteaza rating</option>
            {[1, 2, 3, 4, 5].map(n => (
              <option key={n} value={n}>{n}</option>
            ))}
          </select>
          {fieldErrors.rating && <div className="invalid-feedback">{fieldErrors.rating}</div>}
        </div>
        <button type="submit" className="btn btn-primary">
          {reviewToEdit ? "Salvează modificările" : "Trimite Recenzia"}
        </button>
        {reviewToEdit && onCancelEdit && (
          <button
            type="button"
            className="btn btn-secondary ms-2"
            onClick={onCancelEdit}
          >
            Anuleaza
          </button>
        )}
      </form>
    </div>
  );
};

export default AddReviewForm;
