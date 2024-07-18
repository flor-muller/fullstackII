import React, { useState, useEffect } from "react";
import Button from "@mui/material/Button";
import axios from "axios";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell, { tableCellClasses } from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TablePagination from "@mui/material/TablePagination";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import IconButton from "@mui/material/IconButton";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogTitle from "@mui/material/DialogTitle";
import TextField from "@mui/material/TextField";
import Select from "@mui/material/Select";
import MenuItem from "@mui/material/MenuItem";
import InputLabel from "@mui/material/InputLabel";
import FormControl from "@mui/material/FormControl";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "./admin.events.styles.css";
import { endpointHost } from "../../variables/endpoints";
import {
  getAllCategories,
  getAllEvents,
  getAllPlaces,
} from "../../helpers/axios_helper.js";
import { styled } from "@mui/material/styles";

const ITEM_HEIGHT = 32;
const ITEM_PADDING_TOP = 8;
const MenuProps = {
  PaperProps: {
    style: {
      maxHeight: ITEM_HEIGHT * 4.5 + ITEM_PADDING_TOP,
      width: 250,
    },
  },
};

const StyledTableCell = styled(TableCell)(({ theme }) => ({
  [`&.${tableCellClasses.head}`]: {
    backgroundColor: "#2575fc",
    color: theme.palette.common.white,
  },
  [`&.${tableCellClasses.body}`]: {
    fontSize: 14,
  },
}));

const StyledTableRow = styled(TableRow)(({ theme }) => ({
  "&:nth-of-type(odd)": {
    backgroundColor: theme.palette.action.hover,
  },
  // hide last border
  "&:last-child td, &:last-child th": {
    border: 0,
  },
}));

const AdminEvents = () => {
  const [events, setEvents] = useState([]);
  const [categories, setCategories] = useState([]);
  const [places, setPlaces] = useState([]);
  const [open, setOpen] = useState(false);
  const [editOpen, setEditOpen] = useState(false);
  const [form, setForm] = useState({
    name: "",
    description: "",
    category: "",
    dateEvent: "",
    placeId: "",
    file: null,
  });
  const [editForm, setEditForm] = useState({
    name: "",
    description: "",
    category: "",
    dateEvent: "",
    placeId: "",
    file: null,
  });
  const [selectedEvent, setSelectedEvent] = useState(null);
  const columns = [
    { id: "name", label: "Nombre", minWidth: 170 },
    { id: "description", label: "Descripción", minWidth: 170 },
    { id: "category", label: "Categoría", minWidth: 170 },
    { id: "place", label: "Lugar", minWidth: 170 },
    { id: "actions", label: "Acciones", minWidth: 100 },
  ];

  const [page, setPage] = React.useState(0);
  const [rowsPerPage, setRowsPerPage] = React.useState(10);

  useEffect(() => {
    const fetchInitialData = async () => {
      try {
        const events = await getAllEvents();
        const places = await getAllPlaces();
        const categories = await getAllCategories();
        setEvents(events);
        setPlaces(places);
        setCategories(categories);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };
    fetchInitialData();
  }, []);

  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  const handleEditOpen = (event) => {
    setSelectedEvent(event);
    setEditOpen(true);
  
    // Asegurar que los datos se manejen correctamente
    const formattedDate = event.dateEvent && event.dateEvent.date
      ? new Date(event.dateEvent.date).toISOString().substring(0, 16)
      : "";
  
    const categoryName = event.category ? event.category.name : "";
    const placeName = event.place ? event.place.name : "";
    const imageUrl = event.images ? event.images.url : "";
  
    setEditForm({
      name: event.name || "",
      description: event.description || "",
      category: categoryName,
      dateEvent: formattedDate,
      placeId: placeName,
      currentImage: imageUrl,
    });
  };
  
  
  
  

  const handleEditClose = () => {
    setSelectedEvent(null);
    setEditOpen(false);
  };

  const handleChange = (e) => {
    const { name, value, files } = e.target;
    setForm((prevForm) => ({
      ...prevForm,
      [name]: files ? files[0] : value,
    }));
  };

  const handleEditChange = (e) => {
    const { name, value, files } = e.target;
    setEditForm((prevForm) => ({
      ...prevForm,
      [name]: files ? files[0] : value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    const formData = new FormData();
    const category = categories.find((cat) => cat.name === form.category);
    const place = places.find((place) => place.name === form.placeId);

    const eventDTO = {
      name: form.name,
      description: form.description,
      category: category ? { id: category.id, name: category.name } : null,
      dateEvent: { date: form.dateEvent },
      placeId: place ? place.id : null,
    };

    formData.append("eventDTO", JSON.stringify(eventDTO));

    if (form.file) {
      formData.append("file", form.file, form.file.name);
    }

    // Mostrar en consola lo que se está enviando al backend
    console.log("Datos que se enviarán al backend (FormData):");
    formData.forEach((value, key) => {
      console.log(`${key}: ${value}`);
    });

    const storedData = sessionStorage.getItem(
      "oidc.user:http://54.211.227.50:8080/realms/ticketrek:frontend-ticketrek"
    );
    if (!storedData) {
      console.error("Datos de autenticación no encontrados en sessionStorage");
      return;
    }

    const tokenData = JSON.parse(storedData);
    const token = tokenData.access_token;

    if (!token) {
      console.error("Token no encontrado en sessionStorage");
      return;
    }

    axios
      .post(`${endpointHost}/api/events/event/private/add`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: `Bearer ${token}`,
        },
      })
      .then((response) => {
        console.log("Evento creado:", response.data);
        toast.success("Evento creado con éxito");
        handleClose();
        getAllEvents().then((events) => setEvents(events));
      })
      .catch((error) => {
        console.error("Error creando el evento:", error);
      });
  };

  const handleEditSubmit = (e) => {
    e.preventDefault();
  
    const category = categories.find((cat) => cat.name === editForm.category);
    const place = places.find((place) => place.name === editForm.placeId);
  
    const eventDTO = {
      id: selectedEvent.id,
      name: editForm.name,
      description: editForm.description,
      // Mantener el valor original de dateEvent
      dateEvent: {
        id: selectedEvent.dateEvent.id,
        date: selectedEvent.dateEvent.date,
      },
      // Mantener el valor original de images
      images: {
        id: selectedEvent.images.id,
        url: selectedEvent.images.url,
      },
      category: category ? { id: category.id, name: category.name } : null,
      placeId: place ? place.id : null,
    };
  
    // Log para verificar el objeto eventDTO que se enviará
    console.log("Cuerpo de la solicitud:", JSON.stringify(eventDTO));
  
    const storedData = sessionStorage.getItem(
      "oidc.user:http://54.211.227.50:8080/realms/ticketrek:frontend-ticketrek"
    );
    if (!storedData) {
      console.error("Datos de autenticación no encontrados en sessionStorage");
      return;
    }
  
    const tokenData = JSON.parse(storedData);
    const token = tokenData.access_token;
  
    if (!token) {
      console.error("Token no encontrado en sessionStorage");
      return;
    }
  
    const url = `${endpointHost}/api/events/event/private/update?id=${selectedEvent.id}`;
    console.log("URL de la solicitud:", url);
  
    axios
      .put(url, eventDTO, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      })
      .then((response) => {
        console.log("Evento actualizado:", response.data);
        toast.success("Evento actualizado con éxito");
        handleEditClose();
      })
      .catch((error) => {
        console.error("Error actualizando el evento:", error);
      });
  };
  

  const handleDelete = (eventId) => {
    const storedData = sessionStorage.getItem(
      "oidc.user:http://54.211.227.50:8080/realms/ticketrek:frontend-ticketrek"
    );
    if (!storedData) {
      console.error("Datos de autenticación no encontrados en sessionStorage");
      return;
    }

    const tokenData = JSON.parse(storedData);
    const token = tokenData.access_token;

    if (!token) {
      console.error("Token no encontrado en sessionStorage");
      return;
    }

    axios
      .delete(
        `${endpointHost}/api/events/event/private/deleteById/${eventId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      )
      .then((response) => {
        console.log("Evento eliminado:", response.data);
        toast.success("Evento eliminado con éxito");
        getAllEvents().then((events) => setEvents(events));
      })
      .catch((error) => {
        console.error("Error eliminando el evento:", error);
      });
  };

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(+event.target.value);
    setPage(0);
  };

  return (
    <div>
      <ToastContainer />
      <div className="button-container">
        <Button variant="contained" onClick={handleOpen}>
          +
        </Button>
      </div>
      <div className="table-container">
        <Paper sx={{ width: "95%", overflow: "hidden", marginTop: 5 }}>
          <TableContainer component={Paper}>
            <Table>
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
                {events.length > 0 ? (
                  events
                    .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                    .map((event) => {
                      return (
                        <StyledTableRow
                          hover
                          role="checkbox"
                          tabIndex={-1}
                          key={event.id}
                        >
                          {columns.map((column) => {
                            if (column.id === "actions") {
                              return (
                                <StyledTableCell
                                  key={column.id}
                                  align={column.align}
                                >
                                  <IconButton
                                    onClick={() => handleEditOpen(event)}
                                  >
                                    <EditIcon />
                                  </IconButton>
                                  <IconButton
                                    onClick={() => handleDelete(event.id)}
                                  >
                                    <DeleteIcon />
                                  </IconButton>
                                </StyledTableCell>
                              );
                            } else if (column.id === "place") {
                              return (
                                <StyledTableCell
                                  key={column.id}
                                  align={column.align}
                                >
                                  {event.place.name}{" "}
                                </StyledTableCell>
                              );
                            } else if (column.id === "category") {
                              return (
                                <StyledTableCell
                                  key={column.id}
                                  align={column.align}
                                >
                                  {event.category.name}{" "}
                                </StyledTableCell>
                              );
                            } else {
                              return (
                                <StyledTableCell
                                  key={column.id}
                                  align={column.align}
                                >
                                  {event[column.id]}
                                </StyledTableCell>
                              );
                            }
                          })}
                        </StyledTableRow>
                      );
                    })
                ) : (
                  <TableRow>
                    <TableCell colSpan={5} align="center">
                      Cargando los eventos...
                    </TableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </TableContainer>
          <TablePagination
            rowsPerPageOptions={[10, 25, 100]}
            component="div"
            count={events.length}
            rowsPerPage={rowsPerPage}
            page={page}
            onPageChange={handleChangePage}
            onRowsPerPageChange={handleChangeRowsPerPage}
            labelRowsPerPage="Filas por página"
            labelDisplayedRows={({ from, to, count }) =>
              `${from}-${to} de ${count}`
            }
          />
        </Paper>
        <Dialog open={open} onClose={handleClose}>
          <form onSubmit={handleSubmit}>
            <DialogTitle>Crear nuevo evento</DialogTitle>
            <DialogContent>
              <TextField
                autoFocus
                required
                margin="dense"
                id="name"
                name="name"
                label="Nombre"
                type="text"
                fullWidth
                variant="outlined"
                onChange={handleChange}
              />
              <TextField
                autoFocus
                required
                margin="dense"
                id="description"
                name="description"
                label="Descripción"
                type="text"
                fullWidth
                variant="outlined"
                onChange={handleChange}
              />
              <FormControl fullWidth margin="dense" required>
                <InputLabel id="categoria-label">Categoría</InputLabel>
                <Select
                  labelId="categoria-label"
                  id="category"
                  name="category"
                  label="Categoría"
                  onChange={handleChange}
                  MenuProps={MenuProps}
                >
                  {categories.map((category) => (
                    <MenuItem key={category.id} value={category.name}>
                      {category.name}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
              <TextField
                autoFocus
                required
                margin="dense"
                id="dateEvent"
                name="dateEvent"
                label="Fecha"
                type="datetime-local"
                fullWidth
                variant="outlined"
                onChange={handleChange}
                InputLabelProps={{
                  shrink: true,
                }}
              />
              <FormControl fullWidth margin="dense" required>
                <InputLabel id="ciudad-label">Lugar</InputLabel>
                <Select
                  labelId="ciudad-label"
                  id="placeId"
                  name="placeId"
                  label="Lugar"
                  onChange={handleChange}
                  MenuProps={MenuProps}
                >
                  {places.map((place) => (
                    <MenuItem key={place.id} value={place.name}>
                      {place.name}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
              <TextField
                autoFocus
                required
                margin="dense"
                id="file"
                name="file"
                type="file"
                accept="image/*"
                onChange={handleChange}
                fullWidth
                variant="outlined"
              />
            </DialogContent>
            <DialogActions>
              <Button onClick={handleClose}>Cancelar</Button>
              <Button type="submit">Crear</Button>
            </DialogActions>
          </form>
        </Dialog>
        {/* Modal para editar evento */}
        <Dialog open={editOpen} onClose={handleEditClose}>
          <form onSubmit={handleEditSubmit}>
            <DialogTitle>Editar evento</DialogTitle>
            <DialogContent>
              {selectedEvent && (
                <>
                  <TextField
                    autoFocus
                    required
                    margin="dense"
                    id="name"
                    name="name"
                    label="Nombre"
                    type="text"
                    fullWidth
                    variant="outlined"
                    value={editForm.name}
                    onChange={handleEditChange}
                  />
                  <TextField
                    autoFocus
                    required
                    margin="dense"
                    id="description"
                    name="description"
                    label="Descripción"
                    type="text"
                    fullWidth
                    variant="outlined"
                    value={editForm.description}
                    onChange={handleEditChange}
                  />
                  <FormControl fullWidth margin="dense" required>
                    <InputLabel id="categoria-label">Categoría</InputLabel>
                    <Select
                      labelId="categoria-label"
                      id="category"
                      name="category"
                      label="Categoría"
                      value={editForm.category}
                      onChange={handleEditChange}
                      MenuProps={MenuProps}
                    >
                      {categories.map((category) => (
                        <MenuItem key={category.id} value={category.name}>
                          {category.name}
                        </MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                  <TextField
                    autoFocus
                    margin="dense"
                    id="dateEvent"
                    name="dateEvent"
                    label="Fecha"
                    type="datetime-local"
                    fullWidth
                    variant="outlined"
                    value={editForm.dateEvent}
                    onChange={handleEditChange}
                    InputLabelProps={{
                      shrink: true,
                    }}
                  />
                  <FormControl fullWidth margin="dense" required>
                    <InputLabel id="ciudad-label">Lugar</InputLabel>
                    <Select
                      labelId="ciudad-label"
                      id="placeId"
                      name="placeId"
                      label="Lugar"
                      value={editForm.placeId}
                      onChange={handleEditChange}
                      MenuProps={MenuProps}
                    >
                      {places.map((place) => (
                        <MenuItem key={place.id} value={place.name}>
                          {place.name}
                        </MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                  <TextField
                    autoFocus
                    margin="dense"
                    id="file"
                    name="file"
                    type="file"
                    accept="image/*"
                    onChange={handleEditChange}
                    fullWidth
                    variant="outlined"
                  />
                  {editForm.currentImage && ( // Muestra la imagen actual si está definida
                    <img
                      src={editForm.currentImage}
                      alt="Imagen actual"
                      style={{ maxWidth: "100%", marginTop: 10 }}
                    />
                  )}
                </>
              )}
            </DialogContent>
            <DialogActions>
              <Button onClick={handleEditClose}>Cancelar</Button>
              <Button type="submit">Guardar cambios</Button>
            </DialogActions>
          </form>
        </Dialog>
      </div>
    </div>
  );
};

export default AdminEvents;
