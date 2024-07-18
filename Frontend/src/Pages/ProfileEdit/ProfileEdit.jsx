import React from 'react';
import { Typography } from '@mui/material';
import TextField from "@mui/material/TextField";
import './profile.edit.styles.css';

const ProfileEdit = ({ userData, onChange }) => {
  return (
    <form className="container-profile" id='profile-edit'>
      <div className="card-profile">
        <div className="card-profile-edit-image"></div>
        <div className="card-profile-edit">
            
          <Typography variant="h5" className='card-title-profile-edit'>
            {userData.userName}
          </Typography>

          <TextField
            color="secondary" focused
            label="Nombre"
            variant="outlined"
            type="text"
            name="firstName"
            value={userData.firstName}
            onChange={onChange}
          />

          <TextField
            color="secondary" focused
            label="Apellido"
            variant="outlined"
            type="text"
            name="lastName"
            value={userData.lastName}
            onChange={onChange}
          />
        </div>
      </div>
    </form>
  );
}

export default ProfileEdit;
