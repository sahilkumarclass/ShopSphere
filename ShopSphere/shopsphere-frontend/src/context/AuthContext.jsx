import { createContext, useEffect, useState } from 'react';
import { parseJwt, TOKEN_KEY } from '../utils/tokenUtils';

export const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [ready, setReady] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem(TOKEN_KEY);
    if (token) {
      const claims = parseJwt(token);
      if (claims && claims.exp * 1000 > Date.now()) {
        setUser({
          email: claims.sub,
          role: claims.role,
          userId: claims.userId,
          name: claims.name,
        });
      } else {
        localStorage.removeItem(TOKEN_KEY);
      }
    }
    setReady(true);
  }, []);

  const login = (token) => {
    localStorage.setItem(TOKEN_KEY, token);
    const claims = parseJwt(token);
    setUser({
      email: claims.sub,
      role: claims.role,
      userId: claims.userId,
      name: claims.name,
    });
  };

  const logout = () => {
    localStorage.removeItem(TOKEN_KEY);
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, logout, ready }}>
      {children}
    </AuthContext.Provider>
  );
}
