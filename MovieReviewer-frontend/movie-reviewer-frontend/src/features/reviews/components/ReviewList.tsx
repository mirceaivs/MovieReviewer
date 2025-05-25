
import React from 'react';
import type { Review } from '../types';

interface ReviewListProps {
  reviews: Review[];
  onEdit?: (review: Review) => void;
  onDelete?: (id: number) => void;
  isAdmin?: boolean;         // 👈 ADDED
  currentUser?: string;      // 👈 ADDED
}

const ReviewList: React.FC<ReviewListProps> = ({
  reviews,
  onEdit,
  onDelete,
  isAdmin = false,
  currentUser
}) => {
  if (reviews.length === 0) {
    return <p><i>Nu exista recenzii pentru acest film.</i></p>;
  }
  return (
    <ul className="list-group mb-3">
      {reviews.map(review => (
        <li key={review.id} className="list-group-item d-flex justify-content-between align-items-start">
          <div>
            <strong>{review.username}</strong> – {review.rating}/5 stele
            <div>{review.content}</div>
          </div>
          {(onEdit && onDelete) && (
            (isAdmin || review.username === currentUser) && (
              <div>
               
                {review.username === currentUser && (
                  <button
                    className="btn btn-sm btn-warning me-2"
                    onClick={() => onEdit(review)}
                  >
                    Editeaza
                  </button>
                )}
               
                <button
                  className="btn btn-sm btn-danger"
                  onClick={() => onDelete(review.id!)}
                >
                  Sterge
                </button>
              </div>
            )
          )}
        </li>
      ))}
    </ul>
  );
};

export default ReviewList;
