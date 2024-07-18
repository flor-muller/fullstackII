import React, { useState, useEffect } from 'react';
import { Button, Typography, Dialog, DialogActions, DialogContent, DialogTitle } from '@mui/material';
import { getUserInfo, updateUserInfo } from '../../helpers/axios_helper.js';
import { userManager } from '../../helpers/auth_helper.js';
import Spinner from '../Spinner/Spinner.jsx';
import ProfileEdit from '../../Pages/ProfileEdit/ProfileEdit.jsx';
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import './user.card.styles.css';
import zIndex from '@mui/material/styles/zIndex.js';

const UserCard = () => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [open, setOpen] = useState(false);
  const [userData, setUserData] = useState({
    id: '',
    firstName: '',
    lastName: '',
    email: '',
    userName: ''
  });

  useEffect(() => {
    fetchUserInfo();
  }, []);

  const fetchUserInfo = async () => {
    try {
      const response = await getUserInfo();
      setUser(response.data);
      setUserData({
        id: response.data.id,
        firstName: response.data.name,
        lastName: response.data.lastName,
        email: response.data.email,
        userName: response.data.userName
      });
    } catch (error) {
      console.error('Error al obtener la información del usuario:', error);
      setError('Error al obtener la información del usuario');
    } finally {
      setLoading(false);
    }
  };

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = async (shouldFetchUserInfo = false) => {
    setOpen(false);
    if (shouldFetchUserInfo) {
      await userManager.signinSilent();
      fetchUserInfo();
    }
  };

  const handleSave = async () => {
    try {
      await updateUserInfo(userData);
      toast.success(`Los datos del usuario se han actualizado correctamente!`);
      handleClose(true);
    } catch (error) {
      console.error('Error al actualizar la información del usuario:', error);
      toast.error(`No se pudo actualizar la información del usuario.!`);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setUserData({
      ...userData,
      [name]: value
    });
  };

  if (loading) {
    return <Spinner />;
  }

  if (error) {
    return <div>{error}</div>;
  }

  return (
  <div className='main-user-card'>
    <ToastContainer className="toast-container-center-user" />
    <div className="container-profile" id='details'>
      <div className="card-profile">
        <div className="card-profile-image"></div>
        <div className="card-profile-text">
          <Typography variant="h5" className='card-title-profile'>
            {user.userName}
          </Typography>
          <Typography variant="body1" className='card-text-profile'>
            <strong>Nombre:</strong> {user.name}
          </Typography>
          <Typography variant="body1" className='card-text-profile'>
            <strong>Apellido:</strong> {user.lastName}
          </Typography>
          <Typography variant="body1" className='card-text-profile' mb={3}>
            <strong>Email:</strong> {user.email}
          </Typography>
          <Button variant='outlined' onClick={handleClickOpen}>Editar</Button>
        </div>
      </div>
      <div className="card-profile-flyer"></div>
      <Dialog open={open} onClose={() => handleClose(false)} maxWidth="sm" fullWidth>
        <DialogTitle align="center" color="#2575fc" >Editar Perfil</DialogTitle>
        <DialogContent>
          <ProfileEdit userData={userData} onChange={handleChange} />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => handleClose(false)} color="primary">Cancelar</Button>
          <Button onClick={handleSave} color="primary">Guardar</Button>
        </DialogActions>
      </Dialog>
    </div>
  </div>
  );
}

export default UserCard;
