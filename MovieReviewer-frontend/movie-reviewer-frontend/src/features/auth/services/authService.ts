import API from '../../../axiosClient';
import type { LoginRequest, LoginResponse, RegisterRequest, User } from '../types';

const authService = {
  login: (data: LoginRequest) => {
    return API.post<LoginResponse>('/auth/login', data);
  },
  register: (data: RegisterRequest) => {
    return API.post<User>('/auth/register', data);
  }
};

export default authService;
