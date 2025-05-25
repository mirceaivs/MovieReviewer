import API from '../../../axiosClient';
import type { UserProfile } from '../type';

const getProfile = () => API.get<UserProfile>('/users/me');
const updateProfile = (data: UserProfile) => API.put<void>('/users/me', data);
const deleteProfile = () => API.delete<void>('/users/me');

const userService = { getProfile, updateProfile, deleteProfile };
export default userService;