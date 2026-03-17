import { createContext, useContext, useState } from "react";

const AuthContext = createContext(null);

export default function AuthProvider({ children }) {
    const [user, setUser] = useState(null);
    const [token, setToken] = useState(null);

    const login = (userData, userToken) => {
        setUser(userData);
        setToken(userToken);
    }

    return (
        <AuthContext.Provider value = {{ user, token, login }}>
            {children}
        </AuthContext.Provider>
    );
}

export const useAuth = () => useContext(AuthContext);