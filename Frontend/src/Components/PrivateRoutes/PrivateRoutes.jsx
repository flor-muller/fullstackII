import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { getUser, login } from '../../helpers/auth_helper';
import swal from 'sweetalert';
import Spinner from '../Spinner/Spinner';

function PrivateRoutes({ children }) {
    const location = useLocation();
    const [isAuthenticated, setIsAuthenticated] = useState(false);

    useEffect(() => {
        getUser().then(user => {
            if (user) {
                setIsAuthenticated(true);
            } else {
                swal('Iniciar Sesión', 'Para poder continuar debes iniciar sesión.', 'warning' );
                login();
            }
        });
    }, [location]);

    if (!isAuthenticated) {
        return <Spinner/>; 
    }

    return children;
}

export default PrivateRoutes;

