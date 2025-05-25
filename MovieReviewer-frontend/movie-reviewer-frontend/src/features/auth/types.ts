export interface LoginRequest {
  email: string;
  password: string;
}
export interface LoginResponse {
  token: string;
  expiresIn: number;
}
export interface RegisterRequest {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  username: string;
}
export interface User {
  id: number;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
}
