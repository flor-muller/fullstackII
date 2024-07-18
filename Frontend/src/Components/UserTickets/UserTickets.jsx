import React, { useEffect, useState } from 'react';
import { getUserInfo, getUserPurchases } from '../../helpers/axios_helper';
import './user.tickets.styles.css';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell, { tableCellClasses } from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TablePagination from '@mui/material/TablePagination';
import TableRow from '@mui/material/TableRow';
import { styled } from '@mui/material/styles';
import Spinner from '../Spinner/Spinner';

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

const columns = [
  { id: 'image', label: '', minWidth: 100 },
  { id: 'date', label: 'Fecha', minWidth: 100 },
  { id: 'event', label: 'Evento', minWidth: 100 },
  { id: 'place', label: 'Lugar', minWidth: 100 },
  { id: 'address', label: 'Dirección', minWidth: 100 },
];

const UserTickets = () => {
  const [page, setPage] = React.useState(0);
  const [rowsPerPage, setRowsPerPage] = React.useState(10);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [tickets, setTickets] = useState([]);

  useEffect(() => {
    fetchUserInfo();
  }, []);

  const fetchUserInfo = async () => {
    try {
      const response = await getUserInfo();
      const userId = response.data.id;
      const purchasedTickets = await getUserPurchases(userId);
      setTickets(purchasedTickets.data);
    } catch (error) {
      console.error("Error al obtener la información del usuario", error);
      setError("Error al obtener la información del usuario");
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <Spinner />;
  }

  if (error) {
    return <div>{error}</div>;
  }

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(+event.target.value);
    setPage(0);
  };

  const formatDate = (dateString) => {
    const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
    return new Date(dateString).toLocaleDateString('es-ES', options);
  };

  const createRows = (tickets) => {
    const sortedTickets = tickets.sort((a, b) => {
      const dateA = new Date(a.event.dateEvent.date);
      const dateB = new Date(b.event.dateEvent.date);
      return dateB - dateA;
    });

    return sortedTickets.map(ticket => {
      const { event } = ticket;
      const date = formatDate(event.dateEvent.date);
      const place = event.place ? event.place.name : 'Lugar no disponible';
      const address = event.place
        ? `${event.place.street} ${event.place.number}, ${event.place.neighborhood}`
        : 'Dirección no disponible';
      const image = event.images ? event.images.url : '';

      return {
        image,
        date,
        event: event.name,
        place,
        address
      };
    });
  };

  const rows = createRows(tickets);

  return (
    <div id='tickets' className='user-tickets-container'>
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
              {rows
                .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                .map((row, index) => (
                  <StyledTableRow hover role="checkbox" tabIndex={-1} key={index}>
                    <StyledTableCell>
                      {row.image && <img src={row.image} alt="Evento" style={{ width: 50, height: 'auto' }} />}
                    </StyledTableCell>
                    <StyledTableCell>{row.date}</StyledTableCell>
                    <StyledTableCell>{row.event}</StyledTableCell>
                    <StyledTableCell>{row.place}</StyledTableCell>
                    <StyledTableCell>{row.address}</StyledTableCell>
                  </StyledTableRow>
                ))}
            </TableBody>
          </Table>
        </TableContainer>
        <TablePagination
          rowsPerPageOptions={[10, 25, 100]}
          component="div"
          count={rows.length}
          rowsPerPage={rowsPerPage}
          page={page}
          onPageChange={handleChangePage}
          onRowsPerPageChange={handleChangeRowsPerPage}
          labelRowsPerPage="Filas por página"
          labelDisplayedRows={({ from, to, count }) => `${from}–${to} de ${count}`}
        />
      </Paper>
    </div>
  );
};

export default UserTickets;


