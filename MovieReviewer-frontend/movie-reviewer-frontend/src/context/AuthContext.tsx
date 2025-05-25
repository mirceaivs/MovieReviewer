import React, { createContext, useState, useEffect, useContext } from "react";
import { jwtDecode } from "jwt-decode";


interface UserData {
  username: string;
  roles: string[]; 
}

interface AuthContextProps {
  user: UserData | null;
  token: string | null;
  login: (token: string) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthContextProps>({
  user: null,
  token: null,
  login: () => {},
  logout: () => {},
});

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<UserData | null>(null);
  const [token, setToken] = useState<string | null>(null);

useEffect(() => {
  const storedToken = localStorage.getItem("token");
  if (storedToken) {
    setToken(storedToken);
    const decoded: any = jwtDecode(storedToken);
    setUser({ 
      username: decoded.sub, 
      roles: decoded.roles || [] 
    });
  }
}, []);
const login = (newToken: string) => {
  setToken(newToken);
  localStorage.setItem("token", newToken);
  const decoded: any = jwtDecode(newToken);
  setUser({ 
    username: decoded.sub, 
    roles: decoded.roles || []
  });
};

  const logout = () => {
    setToken(null);
    setUser(null);
    localStorage.removeItem("token");
  };

  return (
    <AuthContext.Provider value={{ user, token, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useIsAdmin = () => {
  const { user } = useAuth();
  return user?.roles?.includes("ROLE_ADMIN") ?? false;
};


export const useAuth = () => useContext(AuthContext);
