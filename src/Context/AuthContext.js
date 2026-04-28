import { createContext, useContext, useState } from "react";

const AuthContext = createContext(null);

export default function AuthProvider({ children }) {
    const [user, setUser] = useState(() => {
        const stored = localStorage.getItem("user");
        return stored ? JSON.parse(stored) : null;
    });
    const [token, setToken] = useState(() => localStorage.getItem("token"));
    const [userId, setUserId] = useState(() => {
        const stored = localStorage.getItem("userId");
        return stored ? JSON.parse(stored) : null;
    });

    const login = (userData, userToken, userIdData) => {
        setUser(userData);
        setToken(userToken);
        setUserId(userIdData);
        localStorage.setItem("user", JSON.stringify(userData));
        localStorage.setItem("token", userToken);
        localStorage.setItem("userId", JSON.stringify(userIdData));
    }

    const logout = () => {
        setUser(null);
        setToken(null);
        setUserId(null);
        localStorage.removeItem("user");
        localStorage.removeItem("token");
        localStorage.removeItem("userId");
    }

    return (
        <AuthContext.Provider value={{ user, token, userId, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

export const useAuth = () => useContext(AuthContext);