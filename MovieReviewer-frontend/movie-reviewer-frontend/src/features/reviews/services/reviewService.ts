import API from '../../../axiosClient';
import type { Review } from '../types';

const reviewService = {
  getByMovie: (movieId: number) => {
    
    return API.get<Review[]>(`/movies/${movieId}/reviews`);
  },
  addReview: (review: { movieId: number; content: string; rating: number }) => {

    return API.post<Review>(`/movies/${review.movieId}/reviews`, {
      content: review.content,
      rating: review.rating,
    
  });
},


  updateReview: (movieId: number, reviewId: number, review: { content: string; rating: number }) => {
    
    return API.put<Review>(`/movies/${movieId}/reviews/${reviewId}`, review);
  },
  deleteReview: (movieId: number, reviewId: number) => {
    
    return API.delete<void>(`/movies/${movieId}/reviews/${reviewId}`);
  },

  getMyReviews: () => API.get<Review[]>('/users/me/reviews'),

};

export default reviewService;
