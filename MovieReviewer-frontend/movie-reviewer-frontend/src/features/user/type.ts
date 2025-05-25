export interface UserProfile {
  id?: number;
  email?: string;
  username?: string;
  firstName?: string;
  lastName?: string;
  bio?: string;
  createdAt?: string;
  password?: string;      // Doar la update
  oldPassword?: string;   // Doar la update
}
