import React, { useState, useEffect } from 'react';
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import Logo from "../../assets/logo_no_bkg.png";
import Button from "@mui/material/Button";
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import AdminPanelSettingsIcon from '@mui/icons-material/AdminPanelSettings';
import Tooltip from '@mui/material/Tooltip';
import { getUser, login, logout, getUserRoles, userManager } from '../../helpers/auth_helper.js';
import "./header.styles.css";

function Header() {
    const navigate = useNavigate();
    const [user, setUser] = useState(null);
    const [roles, setRoles] = useState([]);

    useEffect(() => {
        getUserDetails();
    }, []);

    const getUserDetails = async () => {
        let user = await getUser();
        if (user && user.expired) {
            console.log('Token expired, attempting silent renew...');
            user = await userManager.signinSilent();
        }
        if (user) {
            console.log('Usuario cargado con éxito');
            setUser(user);
            const userRoles = await getUserRoles(user.profile.sub, user.access_token);
            console.log(userRoles);
            setRoles(userRoles);
        } else {
            console.log('El usuario no ha iniciado sesión');
            setUser(null);
            setRoles([]);
        }
    };

    const isAdmin = roles.includes('ADMIN');
    //const isUser = roles.includes('USER'); 

    return (
        <>
            <div className="header-container">
                <div>
                    <Link to="/" style={{ textDecoration: "none" }}>
                        <img src={Logo} alt="Logo" style={{ height: "70px" }} />
                    </Link>
                </div>
                <div className="header-options">
                    {!user && <Button variant="text" onClick={() => login()}>Iniciar Sesión</Button>}
                    {user && <Button variant="text" onClick={() => logout()}>Cerrar Sesión</Button>}
                    {user && (
                        <Tooltip title="Ir a Perfil" slotProps={{ popper: { modifiers: [{ name: 'offset', options: { offset: [0, -8] } }] } }}>
                            <AccountCircleIcon sx={{ color: '#4E45BC', width: 30, height: 30, cursor: 'pointer' }} onClick={() => navigate(`/profile`)} />
                        </Tooltip>
                    )}
                    {user && isAdmin && (
                        <Tooltip title="Administrador" slotProps={{ popper: { modifiers: [{ name: 'offset', options: { offset: [0, -8] } }] } }}>
                            <AdminPanelSettingsIcon sx={{ color: '#4E45BC', width: 30, height: 30, cursor: 'pointer' }} onClick={() => navigate(`/admin`)} />
                        </Tooltip>
                    )}
                </div>
            </div>
        </>
    );
}

export default Header;
