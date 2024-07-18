import React from 'react'
import Construction from '../Construction/Construction'
import axios from 'axios';
import { useNavigate } from "react-router-dom";
import { endpointHost } from '../../variables/endpoints.js';
import DeleteIcon from '@mui/icons-material/Delete';
import IconButton from '@mui/material/IconButton';
import Tooltip from '@mui/material/Tooltip';
import UserList from '../UserList/UserList.jsx';




function stringToColor(string) {
  let hash = 0;
  let i;

  for (i = 0; i < string.length; i += 1) {
      hash = string.charCodeAt(i) + ((hash << 5) - hash);
  }

  let color = '#';

  for (i = 0; i < 3; i += 1) {
      const value = (hash >> (i * 8)) & 0xff;
      color += `00${value.toString(16)}`.slice(-2);
  }

  return color;
}

function stringAvatar(firstname, lastname) {
  const name = `${firstname} ${lastname}`;
  return {
      sx: {
          bgcolor: stringToColor(name),
      },
      children: `${firstname[0]}${lastname[0]}`,
  };
}



const AdminUser = () => {
  return (
    <div id="admin-user">
      <UserList/>
    </div>

  )
}

export default AdminUser