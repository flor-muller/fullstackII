import "./home.styles.css";
import TextField from "@mui/material/TextField";
import InputAdornment from "@mui/material/InputAdornment";
import SearchIcon from "@mui/icons-material/Search";
import MenuItem from "@mui/material/MenuItem";
import { Button } from "@mui/material";
import EventCard from "../EventCard/EventCard";
import { useNavigate } from "react-router";
import { useEffect, useState } from "react";
import { parseDate } from "../utils/utils";
import Spinner from "../Spinner/Spinner.jsx";
import { getAllCategories, getAllCities, getAllEvents, searchEvents } from "../../helpers/axios_helper.js";
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import dayjs from "dayjs";
import utc from 'dayjs/plugin/utc';
import { toast, ToastContainer } from 'react-toastify';
import "react-toastify/dist/ReactToastify.css";
import { RingLoader } from "react-spinners";
dayjs.extend(utc); // Extend Day.js with UTC plugin at the top level


function Home() {
  const [mainEvents, setMainEvents] = useState([]);
  const [cities, setCities] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [eventDate, setEventDate] = useState(null);
  const [eventName, setEventName] = useState(null);
  const [eventCity, setEventCity] = useState(null);
  const [eventCategory, setEventCategory] = useState(null);
  const [eventsNotFound, setEventsNotFound] = useState(false)
  const [searching, setSearching] = useState(false)


  const handleDateChange = (newValue) => {
    if (newValue) {
      const formattedDate = newValue.format("YYYY-MM-DDTHH:mm:ss.SSSZZ"); 
      setEventDate(formattedDate); 
    } else {
      setEventDate(null); 
    }
  };

  useEffect(() => {
    const fetchInitialData = async () => {
      try {
        // if(mainEvents){
        //   return;
        // }
        const events = await getAllEvents();
        const cities = await getAllCities();
        const categories = await getAllCategories();
        setMainEvents(events);
        setCities(cities);
        setCategories(categories);
        setLoading(false);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };
    fetchInitialData();
  }, []);

  const handleSearch = async () => {
    setSearching(true)
    const result = await searchEvents(eventName, eventCity, eventCategory, eventDate);
    console.log(result);
    setSearching(false)
    if (!result  || result.length === 0){
      toast.warn("No encontramos el evento que estabas buscando!", {
        position: "bottom-center"
      });
    }
    if (result && result.length > 0) {
      setMainEvents(result);
    }
  }

  const navigate = useNavigate();
  
  const handleEventClick = (id) => {
    navigate(`/event/detail/${id}`);
  };

  if (loading) {
    return <Spinner/>
  }

  return (
    <div className="main">
      <div id="home" className="cover">
        <div className="search-form-container">
          <TextField
            label="Buscar eventos"
            placeholder="Buscar eventos"
            id="outlined-start-adornment"
            sx={{ m: 1, width: "50ch" }}
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <SearchIcon />
                </InputAdornment>
              ),
            }}
            value={eventName} 
            onChange={(e) => setEventName(e.target.value)} 
          />
          <TextField
            id="outlined-select-currency"
            select
            label="Categoría"
            placeholder="Categoría"
            defaultValue="Categoría"
            sx={{ m: 1, width: "40ch" }}
            value={eventCategory} // Bind value to eventCategory
            onChange={(e) => setEventCategory(e.target.value)} // Update eventCategory
          >
            <MenuItem value={null}>
              {'Ninguna'}
            </MenuItem>
            {categories.map((option) => (
              <MenuItem key={option.id} value={option.name}>
                {option.name}
              </MenuItem>
            ))}
          </TextField>
          <TextField
            id="outlined-select-currency"
            select
            label="Seleccione ciudad"
            placeholder="Seleccione ciudad"
            defaultValue="Selecciona la ciudad"
            sx={{ m: 1, width: "40ch" }}
            value={eventCity}
            onChange={(e) => setEventCity(e.target.value)}
          >
            <MenuItem value={null}>
              {'Ninguna'}
            </MenuItem>
            {cities.map((option) => (
              <MenuItem key={option.id} value={option.name}>
                {option.name}
              </MenuItem>
            ))}
          </TextField>
          <LocalizationProvider dateAdapter={AdapterDayjs} style={{ display: 'flex', alignItems: 'center' }}>
            <DatePicker
              sx={{ m: 1, width: "40ch" }}
              label="Seleccionar Fecha"
              value={eventDate ? dayjs(eventDate) : null} // Convert string to Dayjs object
              onChange={handleDateChange}
              renderInput={(params) => <TextField {...params} />}
            />
          </LocalizationProvider>
          <Button variant="contained" size="small" className="search-button" onClick={()=> handleSearch()}>
            Buscar evento
          </Button>
        </div>
      </div>
      <>
        {
        searching ? 
        <div className="search-loading">
          <RingLoader 
            color="#4e45bc"
            searching
            //size={80}
            speedMultiplier={1.2}/>
        </div> : undefined
        }
      </>
      <div className="events-container">
        {mainEvents.map((ev, index) => (
          <div className="card-container" >
            <EventCard
              className="ev-card"
              key={index}
              id={ev.id}
              imgUrl={ev.images.url}
              title={ev.name}
              minimumPrice={'5000'}
              dateTime={parseDate(ev.dateEvent.date)}
              location={ev.place.name}
              onClick={() => handleEventClick(ev.id)}
            />
          </div>
        ))}
      </div>
      <ToastContainer />
    </div>
  );
}

export default Home;
