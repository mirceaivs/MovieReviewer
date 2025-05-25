
export interface Page<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  number: number;
  size: number;
}

export interface RatingDTO {
  source: string;
  value: string;
}

export interface Movie {
  id: number;
  externalId?: string;
  title: string;
  year?: string;
  rated?: string;
  released?: string;
  runtime?: string;
  genres: string[];
  director?: string;
  writer?: string;
  actors: string[];
  plot?: string;
  language?: string;
  country?: string;
  awards?: string;
  poster?: string;
  ratings?: RatingDTO[];
  metascore?: string;
  imdbRating?: string;
  imdbVotes?: string;
  type?: string;
  response?: string;
}


export interface OmdbSearchMovie {
  imdbID: string;
  title: string;
  year?: string;
  poster?: string;
}






