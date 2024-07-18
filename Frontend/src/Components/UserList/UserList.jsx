import React, { useState, useEffect } from 'react';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell, { tableCellClasses } from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TablePagination from '@mui/material/TablePagination';
import TableRow from '@mui/material/TableRow';
import IconButton from "@mui/material/IconButton";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import { styled } from '@mui/material/styles';
import './user.list.styles.css';
import { deleteUserById, getAllUsersFromKeycloak, deleteUserRole, addUserRole } from '../../helpers/axios_helper';
import { getUser, getUserRoles } from '../../helpers/auth_helper';
import { toast } from 'react-toastify';
import ConfirmDialog from '../ConfirmDialog/ConfirmDialog.jsx';

const StyledTableCell = styled(TableCell)(({ theme }) => ({
  [`&.${tableCellClasses.head}`]: {
    backgroundColor: '#2575fc',
    color: theme.palette.common.white,
  },
  [`&.${tableCellClasses.body}`]: {
    fontSize: 14,
  },
}));

const StyledTableRow = styled(TableRow)(({ theme }) => ({
  '&:nth-of-type(odd)': {
    backgroundColor: theme.palette.action.hover,
  },
  // hide last border
  '&:last-child td, &:last-child th': {
    border: 0,
  },
}));

const UserList = () => {
  const columns = [
    { id: 'userName', label: 'Usuario', minWidth: 170 },
    { id: 'firstName', label: 'Nombre', minWidth: 170 },
    { id: 'lastName', label: 'Apellido', minWidth: 170 },
    { id: 'email', label: 'Email', minWidth: 170 },
    { id: 'role', label: 'Rol', minWidth: 100 },
    { id: 'actions', label: 'Acciones', minWidth: 100 },
  ];

  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [users, setUsers] = useState([]);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [dialogMessage, setDialogMessage] = useState('');
  const [dialogTitle, setDialogTitle] = useState('');
  const [dialogAction, setDialogAction] = useState(null);

  useEffect(() => {
    fetchUserList();
  }, []);

  const fetchUserList = async () => {
    try {
      const response = await getAllUsersFromKeycloak();
      const adminUser = await getUser();
      const adminToken = adminUser.access_token;

      const usersWithRoles = await Promise.all(
        response.data.map(async (user) => {
          const roles = await getUserRoles(user.id, adminToken);
          const isAdmin = roles.includes('ADMIN');
          const finalRole = isAdmin ? 'ADMIN' : 'USER';
          return { ...user, role: finalRole };
        })
      );
      setUsers(usersWithRoles);
      console.log(users);
    } catch (error) {
      console.error('Error al obtener el listado de usuarios:', error);
    }
  };

  const deleteUser = async (idUser) => {
    try {
      await deleteUserById(idUser);
      setUsers(users.filter((user) => user.id !== idUser));
      toast.success("Usuario eliminado exitosamente!");
      console.log(`Usuario con ID ${idUser} eliminado exitosamente.`);
    } catch (error) {
      console.error("Error al eliminar el usuario");
    }
  };

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(+event.target.value);
    setPage(0);
  };

  const updateUserRole = (userId, newRole) => {
    setUsers(users.map((user) => (user.id === userId ? { ...user, role: newRole } : user)));
  };

  const showConfirmDialog = (message, title, action) => {
    setDialogMessage(message);
    setDialogTitle(title);
    setDialogAction(() => action);
    setDialogOpen(true);
  };

  const handleConfirmDialogClose = () => {
    setDialogOpen(false);
    setDialogAction(null);
  };

  const handleConfirmDialogConfirm = async () => {
    if (dialogAction) {
      await dialogAction();
    }
    handleConfirmDialogClose();
  };

  const updateRole = (user, updateUserRole) => {
    const isAdmin = user.role === 'ADMIN';
    const newRole = isAdmin ? 'USER' : 'ADMIN';
    const actionText = isAdmin ? 'ELIMINAR el rol de ADMINISTRADOR' : 'AGREGAR el rol de ADMINISTRADOR';
    const confirmMessage = `¿Deseas ${actionText}?`;
    const confirmTitle = 'Editar Rol';

    const confirmAction = async () => {
      try {
        if (isAdmin) {
          await deleteUserRole(user.id, 'ADMIN');
        } else {
          await addUserRole(user.id, 'ADMIN');
        }
        updateUserRole(user.id, newRole);
        toast.success(`Rol actualizado exitosamente a ${newRole}!`);
      } catch (error) {
        console.error(`Error al ${actionText}`, error);
        toast.error(`Error al ${actionText}`);
      }
    };

    showConfirmDialog(confirmMessage, confirmTitle, confirmAction);
  };

  const confirmDeleteUser = (userId) => {
    const confirmMessage = "¿Deseas eliminar este usuario?";
    const confirmTitle = "Eliminar Usuario";
    const confirmAction = () => deleteUser(userId);

    showConfirmDialog(confirmMessage, confirmTitle, confirmAction);
  };

  return (
    <div className='user-list-container'>
      <Paper sx={{ width: '95%', overflow: 'hidden', marginTop: 5 }}>
        <TableContainer sx={{ maxHeight: 440 }}>
          <Table stickyHeader aria-label="sticky table">
            <TableHead>
              <TableRow>
                {columns.map((column) => (
                  <StyledTableCell
                    key={column.id}
                    align={column.align}
                    style={{ minWidth: column.minWidth }}
                  >
                    {column.label}
                  </StyledTableCell>
                ))}
              </TableRow>
            </TableHead>
            <TableBody>
              {users.length > 0 ? (
                users.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map((user) => (
                  <StyledTableRow hover role="checkbox" tabIndex={-1} key={user.id}>
                    {columns.map((column) => {
                      if (column.id === 'actions') {
                        return (
                          <StyledTableCell key={column.id} align={column.align}>
                            <IconButton onClick={() => updateRole(user, updateUserRole)}>
                              <EditIcon />
                            </IconButton>
                            <IconButton onClick={() => confirmDeleteUser(user.id)}>
                              <DeleteIcon />
                            </IconButton>
                          </StyledTableCell>
                        );
                      } else {
                        return (
                          <StyledTableCell key={column.id} align={column.align}>
                            {user[column.id]}
                          </StyledTableCell>
                        );
                      }
                    })}
                  </StyledTableRow>
                ))
              ) : (
                <TableRow>
                  <TableCell colSpan={6} align='center'>
                    Cargando los usuarios...
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </TableContainer>
        <TablePagination
          rowsPerPageOptions={[10, 25, 100]}
          component="div"
          count={users.length}
          rowsPerPage={rowsPerPage}
          page={page}
          onPageChange={handleChangePage}
          onRowsPerPageChange={handleChangeRowsPerPage}
          labelRowsPerPage="Filas por página"
          labelDisplayedRows={({ from, to, count }) => `${from}–${to} de ${count}`}
        />
      </Paper>
      <ConfirmDialog
        open={dialogOpen}
        message={dialogMessage}
        title={dialogTitle}
        onConfirm={handleConfirmDialogConfirm}
        onCancel={handleConfirmDialogClose}
      />
    </div>
  );
};

export default UserList;
