import API from '../../../axiosClient';
import type { Genre } from '../types';

const genreService = {
  getAll: () => {
    // GET /genres - listă toate genurile
    return API.get<Genre[]>('/genres');
  },
  create: (genre: { name: string }) => {
    // POST /genres - creează gen (necesită ADMIN)
    return API.post<Genre>('/genres', genre);
  },
  delete: (id: number) => {
    // DELETE /genres/{id} - șterge gen (necesită ADMIN)
    return API.delete<void>(`/genres/${id}`);
  },
  update: (id: number, genre: Genre) => {
    // PUT /genres/{id} - actualizează gen (necesită ADMIN)
    return API.put<Genre>(`/genres/${id}`, genre);
  }
};

export default genreService;
