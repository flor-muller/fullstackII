import "./reports.styles.css";
import { useEffect, useState } from "react";
import { Button } from "@mui/material";
import dayjs from "dayjs";
import utc from "dayjs/plugin/utc";
import "react-toastify/dist/ReactToastify.css";
import MenuItem from "@mui/material/MenuItem";
import InputLabel from "@mui/material/InputLabel";
import FormControl from "@mui/material/FormControl";
import Select from "@mui/material/Select";
import {
  getAllCategories,
  getAllEvents,
  getAllPlaces,
  getAllCities,
} from "../../helpers/axios_helper.js";
import Paper from "@mui/material/Paper";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell, { tableCellClasses } from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TablePagination from "@mui/material/TablePagination";
import TableRow from "@mui/material/TableRow";
import { styled } from "@mui/material/styles";
import { jsPDF } from "jspdf";
import logo from "../../assets/logo_no_bkg.png";
import "jspdf-autotable";
import { endpointHost } from "../../variables/endpoints";
import { Menu } from "@mui/material";
import { CircularProgress } from "@mui/material";
import * as XLSX from "xlsx";

dayjs.extend(utc);

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
  "&:last-child td, &:last-child th": {
    border: 0,
  },
}));

const Reports = () => {
  const [events, setEvents] = useState([]);
  const [categories, setCategories] = useState([]);
  const [places, setPlaces] = useState([]);
  const [cities, setCities] = useState([]);
  const [selectedCity, setSelectedCity] = useState("");
  const [selectedCategory, setSelectedCategory] = useState("");
  const [selectedEvent, setSelectedEvent] = useState("");
  const [selectedPlace, setSelectedPlace] = useState("");
  const [reportData, setReportData] = useState(null);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [loadingReport, setLoadingReport] = useState(false);

  useEffect(() => {
    const fetchInitialData = async () => {
      try {
        const events = await getAllEvents();
        const places = await getAllPlaces();
        const categories = await getAllCategories();
        const cities = await getAllCities();
        setEvents(events);
        setPlaces(places);
        setCategories(categories);
        setCities(cities);
      } catch (error) {
        console.error("Error fetching data:", error);
      } 
    };
    fetchInitialData();
  }, []);

  const handleCityChange = (event) => {
    setSelectedCity(event.target.value);
  };

  const handleCategoryChange = (event) => {
    setSelectedCategory(event.target.value);
  };

  const handleEventChange = (event) => {
    setSelectedEvent(event.target.value);
  };

  const handlePlaceChange = (event) => {
    setSelectedPlace(event.target.value);
  };

  const handleReportClick = async () => {
    setLoadingReport(true);
    const reportData = {
      city: selectedCity,
      category: selectedCategory,
      eventName: selectedEvent,
      place: selectedPlace,
    };

    const storedData = sessionStorage.getItem(
      "oidc.user:http://54.211.227.50:8080/realms/ticketrek:frontend-ticketrek"
    );
    if (!storedData) {
      console.error("Datos de autenticación no encontrados en sessionStorage");
      setLoadingReport(false);
      return;
    }

    const tokenData = JSON.parse(storedData);
    const token = tokenData.access_token;

    if (!token) {
      console.error("Token no encontrado en sessionStorage");
      setLoadingReport(false)
      return;
    }
    console.log("selected", selectedEvent);
    console.log("selected", reportData);

    try {
      const response = await fetch(
        `${endpointHost}/api/ticket/tickets/report`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify(reportData),
        }
      );

      if (response.status === 204) {
        setReportData({ tickets: [] });
        setLoadingReport(false);
        return;
      }

      const result = await response.json();
      setReportData(result);
      setLoadingReport(false)
      console.log("Report data:", result);
    } catch (error) {
      console.error("Error fetching report:", error);
      setLoadingReport(false)
    }
  };

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(+event.target.value);
    setPage(0);
  };

  const columns = [
    { id: "eventName", label: "Nombre", minWidth: 170 },
    { id: "place", label: "Lugar", minWidth: 170 },
    { id: "ticketPrice", label: "Precio", minWidth: 170 },
    { id: "eventDate", label: "Fecha", minWidth: 170 },
  ];

  const handleDownloadPDF = () => {
    const doc = new jsPDF();
    const imgData = logo;
    const logoWidth = 50;
    const logoHeight = 20;
    const pageWidth = doc.internal.pageSize.getWidth();
    const centerX = (pageWidth - logoWidth) / 2;
    const logoY = 10;

    doc.addImage(imgData, "PNG", centerX, logoY, logoWidth, logoHeight);

    const tableY = logoY + logoHeight + 20;
    doc.autoTable({ html: "#report-table", startY: tableY });

    doc.save("Ticketrek.pdf");
  };

  const handleDownloadExcel = () => {
    const table = document.getElementById("report-table");
    const wb = XLSX.utils.table_to_book(table, { sheet: "Sheet1" });
    XLSX.writeFile(wb, "report.xlsx");
  };

  const [anchorEl, setAnchorEl] = useState(null);

  const handleMenuOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const handleDownloadClick = (format) => {
    handleMenuClose(); 
    if (format === "pdf") {
      handleDownloadPDF();
    } else if (format === "excel") {
      handleDownloadExcel();
    }
  };

  return (
    <div className="main">
      <div className="form-section">
        <FormControl margin="dense">
          <InputLabel id="ciudad-label">Ciudad</InputLabel>
          <Select
            labelId="city-label"
            id="city"
            name="city"
            label="city"
            sx={{ m: 1, width: "30ch" }}
            MenuProps={MenuProps}
            value={selectedCity}
            onChange={handleCityChange}
          >
            <MenuItem value={""}>
              {'Ninguna'}
            </MenuItem>
            {cities.map((city) => (
              <MenuItem key={city.id} value={city.name}>
                {city.name}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
        <FormControl margin="dense">
          <InputLabel id="categoria-label">Categoría</InputLabel>
          <Select
            labelId="categoria-label"
            id="category"
            name="category"
            label="Categoría"
            sx={{ m: 1, width: "30ch" }}
            MenuProps={MenuProps}
            value={selectedCategory}
            onChange={handleCategoryChange}
          >
            <MenuItem value={""}>
              {'Ninguna'}
            </MenuItem>
            {categories.map((category) => (
              <MenuItem key={category.id} value={category.name}>
                {category.name}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
        <FormControl margin="dense">
          <InputLabel id="event-label">Evento</InputLabel>
          <Select
            labelId="event-label"
            id="event"
            name="event"
            label="event"
            sx={{ m: 1, width: "30ch" }}
            MenuProps={MenuProps}
            value={selectedEvent}
            onChange={handleEventChange}
          >
            <MenuItem value={""}>
              {'Ninguno'}
            </MenuItem>
            {events.map((event) => (
              <MenuItem key={event.id} value={event.name}>
                {event.name}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
        <FormControl margin="dense">
          <InputLabel id="place-label">Lugar</InputLabel>
          <Select
            labelId="place-label"
            id="outlined-place"
            name="place"
            label="place"
            sx={{ m: 1, width: "30ch" }}
            MenuProps={MenuProps}
            value={selectedPlace}
            onChange={handlePlaceChange}
          >
            <MenuItem value={""}>
              {'Ninguno'}
            </MenuItem>
            {places.map((place) => (
              <MenuItem key={place.id} value={place.name}>
                {place.name}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
        <div className="button-section">
          <Button
            variant="contained"
            size="medium"
            className="search-button"
            onClick={handleReportClick}
          >
            Ver Reporte
          </Button>
        </div>
      </div>
      <div className="report-section">
      <div className="card-report-action-container">
          <Button className="report-card-actions">
            <div className="card-report-action-item">
              <h2 style={{margin:0}}>{reportData?.totalTickets}</h2>
              <div>Tickets</div>
            </div>
          </Button>
          <Button className="report-card-actions">
            <div className="card-report-action-item">
              <h2 style={{margin:0}}>{reportData?.totalDistinctEvents}</h2>
              <div>Eventos</div>
            </div>
          </Button>
          <Button className="report-card-actions">
            <div className="card-report-action-item">
              <h2 style={{margin:0}}>{reportData?.totalRevenue}</h2>
              <div>Recaudos ($)</div>
            </div>
          </Button>
          </div>
        <div className="button-download-section">
          {reportData &&
            reportData.tickets &&
            reportData.tickets.length > 0 && (
              <>
                <Button
                  id="download"
                  variant="contained"
                  size="medium"
                  className="search-button"
                  onClick={handleMenuOpen}
                >
                  Descargar reporte
                </Button>
                <Menu
                  anchorEl={anchorEl}
                  open={Boolean(anchorEl)}
                  onClose={handleMenuClose}
                >
                  <MenuItem onClick={() => handleDownloadClick("pdf")}>
                    Descargar en PDF
                  </MenuItem>
                  <MenuItem onClick={() => handleDownloadClick("excel")}>
                    Descargar en Excel
                  </MenuItem>
                </Menu>
              </>
            )}
        </div>

        <div className="report-list-container">
        {loadingReport ? (
            <div style={{ display: "flex", justifyContent: "center", marginTop: "20px" }}>
              <CircularProgress />
            </div>
          ) : (
          reportData && reportData.tickets && reportData.tickets.length > 0 ? (
            <Paper sx={{ width: "95%", overflow: "hidden", marginTop: 5 }}>
              <TableContainer sx={{ maxHeight: 440 }}>
                <Table stickyHeader aria-label="sticky table" id="report-table">
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
                    {reportData.tickets
                      .slice(
                        page * rowsPerPage,
                        page * rowsPerPage + rowsPerPage
                      )
                      .map((ticket) => (
                        <StyledTableRow
                          hover
                          role="checkbox"
                          tabIndex={-1}
                          key={ticket.id}
                        >
                          {columns.map((column) => {
                            const value = ticket[column.id];
                            return (
                              <StyledTableCell
                                key={column.id}
                                align={column.align}
                              >
                                {column.id === "eventDate"
                                  ? dayjs(value).format("DD/MM/YYYY")
                                  : value}
                              </StyledTableCell>
                            );
                          })}
                        </StyledTableRow>
                      ))}
                  </TableBody>
                </Table>
              </TableContainer>
              <TablePagination
                rowsPerPageOptions={[10, 25, 100]}
                component="div"
                count={reportData.tickets.length}
                rowsPerPage={rowsPerPage}
                page={page}
                onPageChange={handleChangePage}
                onRowsPerPageChange={handleChangeRowsPerPage}
                labelRowsPerPage='Filas por página'
                labelDisplayedRows={({ from, to, count}) => `${from}-${to} de ${count}`}
              />
            </Paper>
          ) : (
            <div className="no-tickets-message">No se encontraron tickets para el criterio de búsqueda</div>
          )
          )}
        </div>
      </div>
    </div>
  );
};

export default Reports;
