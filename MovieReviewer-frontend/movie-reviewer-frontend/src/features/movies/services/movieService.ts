
import API from '../../../axiosClient'; 
import type { Movie } from '../types';

const searchOmdb = (query: string, page: number = 0) =>
  API.get('/movies/omdb-search', { params: { query, page: page + 1 } });

const getByImdb = (imdbId: string) =>
  API.get(`/movies/by-imdb/${imdbId}`);

const getAllMovies = (page: number = 0, size: number = 20, sort: string = 'title,asc') =>
  API.get('/movies', { params: { page, size, sort } });


const searchMovies = ({
  title = "",
  genres = [],
  actors = [],
  page = 0,
  size = 20,
  sort = "title,asc"
}: {
  title?: string;
  genres?: string[];
  actors?: string[];
  page?: number;
  size?: number;
  sort?: string;
}) =>
  API.get('/movies/search', {
    params: {
      title,
      genres,
      actors,
      page,
      size,
      sort
    }
  });

const getById = (id: number) => API.get<Movie>(`/movies/${id}`);

const createMovie = (movie: Movie) => API.post<Movie>('/movies', movie);

const updateMovie = (id: number, movie: Partial<Movie>) =>
  API.patch<Movie>(`/movies/${id}`, movie);

const deleteMovie = (id: number) => API.delete<void>(`/movies/${id}`);

const getMovies = (params: any) => API.get('/movies/search', { params });

const movieService = {
  searchOmdb,   
  getByImdb,    
  getAllMovies, 
  searchMovies, 
  getById,      
  createMovie,
  updateMovie,
  deleteMovie,
  getMovies,    
};

export default movieService;
