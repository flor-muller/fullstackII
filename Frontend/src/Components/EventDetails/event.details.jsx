import { useNavigate, useParams } from "react-router";
import "./event.details.styles.css";
import TodayIcon from '@mui/icons-material/Today';
import StadiumIcon from '@mui/icons-material/Stadium';
import PlaceIcon from '@mui/icons-material/Place';
import { useEffect, useState } from "react";
import { FormControl, FormControlLabel, FormLabel, Radio, RadioGroup } from "@mui/material";
import { PulseLoader, RingLoader } from "react-spinners";
import { toast, ToastContainer } from 'react-toastify';
import "react-toastify/dist/ReactToastify.css";
import { createTicket, getEventById, getEventPlace, getPaymentMethods } from "../../helpers/axios_helper";
import { login } from "../../helpers/auth_helper";

function EventDetails() {
    const { eventId } = useParams();
    const [eventDetailData, setEventDetailData] = useState(null);
    const [placeData, setPlaceData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [modal, setModal] = useState(false);
    const [selectedTickets, setSelectedTickets] = useState(false);
    const [paymentMethods, setPaymentMethods] = useState(false);
    const [showPulse, setShowPulse] = useState(false)
    const [userToken, setUserToken] = useState(null)
    const [selectedPaymentMethod, setSelectedPaymentMethod] = useState(null);


    const navigate = useNavigate();

    const updateQuantity = (id,quantity) => {
        const updatedTickets = selectedTickets.map(ticket =>
            ticket.id === id ? { ...ticket, quantity: quantity } : ticket
        );
        setSelectedTickets(updatedTickets);
    }

    const handleConfirmedPurchase = async () => {
        if (userToken) {
            setShowPulse(true)
            const selectedPaymentMethodId = document.querySelector('input[name="radio-buttons-group"]:checked')?.value;
            const zoneQuantity = selectedTickets.filter(ticket => ticket.quantity > 0);
            console.log(`calling create ticket with: ${zoneQuantity}, ${selectedPaymentMethodId}, ${userToken}`)
            const result = await createTicket(zoneQuantity, selectedPaymentMethodId, userToken.profile.sub, eventId);
            setShowPulse(false)
            toast.success('Pago Confirmado', {position: "top-center"})
            toggleModal()
            setTimeout(()=>{
                navigate('/')
            },5000)
        }
        else {
            login()
        }
    }

    const toggleModal = () => {
      setModal(!modal);
    };

    const handleBuyButtonClicked = () => {
        if (selectedTickets && selectedTickets.filter(ticket => ticket.quantity > 0)?.length > 0) {
            toggleModal()
        }
        else {
            toast.warn("Debes Seleccionar Entradas!", {
                position: "top-center"
              });
        }
    }

    if(modal) {
      document.body.classList.add('active-modal')
    } else {
      document.body.classList.remove('active-modal')
    }

    useEffect(() => {
        const fetchInitialData = async () => {
            try {
                const eventDetailObj = await getEventById(eventId);
                setEventDetailData(eventDetailObj);
                setUserToken(JSON.parse(sessionStorage.getItem('oidc.user:http://54.211.227.50:8080/realms/ticketrek:frontend-ticketrek')));
                if (eventDetailObj?.place?.id) {
                    const placeDataObj = await getEventPlace(eventDetailObj.place.id);
                    setPlaceData(placeDataObj);
                    const initialSelectedTickets = placeDataObj.zones.map((zone) => ({
                        id: zone.id,
                        name: zone.name,
                        price: zone.seats[0]?.price,
                        quantity: 0
                    }));
                    setSelectedTickets(initialSelectedTickets);
                }
                setLoading(false);
                const availableMethodsObj = await getPaymentMethods();
                setPaymentMethods(availableMethodsObj)
            } catch (error) {
                console.error('Error fetching data:', error);
                setError('Error fetching data');
                setLoading(false);
            }
        };
        fetchInitialData();
    }, [eventId]);

    if (loading) {
        return <div className="loading-div">
            <RingLoader
                color="#4e45bc"
                loading
                size={80}
                speedMultiplier={1.2}
                />
            </div>;
    }

    if (error) {
        return <div className="error-div">
            <p>
            {error}
            </p>
            </div>;
    }

    if (!eventDetailData || !placeData) {
        return <div>No event data available</div>;
    }

    const formattedDescription = eventDetailData.description.split('\n').map((line, index) => (
        <span key={index}>
            {line.trim()}
            <br />
        </span>
    ));

    return (
        <div className="main-event-details">
            <div className='event-detail header'>
                <div className="event-detail title">
                    <h2>{eventDetailData.name ?? ''}</h2>
                    <h4><TodayIcon /> {new Date(eventDetailData.dateEvent.date).toLocaleString()}</h4>
                </div>
                <div className="event-detail location">
                    <h3 className="location-item"><StadiumIcon /> {placeData.name ?? ''}</h3>
                    <p className="location-item"><PlaceIcon /> {placeData.street}, {placeData.number}, {placeData.city.name ?? ''}</p>
                </div>
            </div>
            <div className="main-details">
                <div className="tickets">
                    <div className="event-detail ticket-types">
                        <div className="ticket-grid">
                            <div className="ticket-grid-header">
                                <h5 className="type-column">Tipo De Entrada</h5>
                                <h5 className="price-column">Precio</h5>
                                <h5 className="quantity-column">Cantidad</h5>
                            </div>
                            <div className="types-data">
                                {placeData.zones.map(ticket => (
                                    <div key={ticket.id} className="ticket-grid-row">
                                        <div className="type">
                                            <h4>{ticket.name ?? ''}</h4>
                                            <p className="ticket-description">{ticket.description ?? 'Descripcion Ticket y Beneficios'}</p>
                                            { ticket.seats[0]?.availability !== 1 ? <p className="soldout-message">Tickets Agotados!</p> : undefined}
                                        </div>
                                        <div className="price">
                                            <p>${ticket.seats[0]?.price ?? 'N/A'}</p>
                                        </div>
                                        {/* <div className="quantity">
                                            <input disabled={ticket.seats[0]?.availability === 1 ? false : true} type="number" id={`quantity-${ticket.id}`} defaultValue="0" name="quantity" min="0" max="10" step="1" />
                                        </div> */}
                                        <div className="quantity">
                                            <select
                                                id={`quantity-${ticket.id}`}
                                                name="quantity"
                                                defaultValue="0"
                                                disabled={ (ticket.seats.find(seat => seat.availability === 1)) ? false : true}
                                                onChange={(e)=> updateQuantity(ticket.id, e.target.value)}
                                            >
                                                {[...Array(ticket.availability > 10 ? 10 : ticket.availability).keys()].map(value => (
                                                    <option key={value} value={value}>
                                                        {value}
                                                    </option>
                                                ))}
                                            </select>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>
                    </div>
                    <div>
                        <button className="event-detail buy-button" onClick={handleBuyButtonClicked}>COMPRAR</button>
                    </div>
                    <div className="event-detail description">
                        <p>{formattedDescription}</p>
                    </div>
                </div>
                <div className="banner">
                    <img id='banner-img' src={eventDetailData.images.url} alt="Event Banner" />
                </div>
            </div>
            <ToastContainer />
            {modal && (
            <div className="modal">
              <div onClick={toggleModal} className="overlay"></div>
              <div className="modal-content">
                <h2>Confirmar Compra</h2>
                <div className="selected-ticket-types">
                                <div className="selected-ticket-type-header">
                                    <p id="ticket-header">Ticket</p>
                                    <p id="ticket-header">Cantidad</p>
                                    <p id="ticket-header">Subtotal</p>
                                </div>
                    { selectedTickets.filter(ticket=>ticket.quantity > 0).map(ticket=>(
                                <div className="selected-ticket-type">
                                    <p>{ticket.name}</p>
                                    <p>{ticket.quantity}</p>
                                    <p>${ticket.price * ticket.quantity}</p>
                                </div>
                                ))
                    }
                </div>
                <div className="total-div">
                    <h3>Total: $
                        {selectedTickets
                            .filter(ticket => ticket.quantity > 0)
                            .reduce((total, ticket) => total + (ticket.quantity * ticket.price), 0)}
                    </h3>
                </div>
                <div className="payment-methods">
                    <FormControl>
                        <FormLabel id="demo-radio-buttons-group-label">Metodo De Pago</FormLabel>
                        <RadioGroup
                            onChange={(event) => setSelectedPaymentMethod(event.target.value)} // Update state on change
                            aria-labelledby="demo-radio-buttons-group-label"
                            defaultValue=""
                            name="radio-buttons-group"
                        >
                            {paymentMethods.map(method=>(
                            <FormControlLabel value={method.id} control={<Radio />} label={method.category + " " + method.detail} />
                            ))}
                        </RadioGroup>
                    </FormControl>
                </div>
                <div>
                    <button className="event-detail buy-button" onClick={handleConfirmedPurchase} disabled={showPulse}>COMPRAR</button>
                </div>
                <button className="close-modal close-button"  onClick={toggleModal}>
                  Cancelar
                </button>
                <PulseLoader
                color="#4e45bc"
                margin={30}
                size={10}
                loading={showPulse}
                />
              </div>
            </div>
          )}
        </div>
    )
}

export default EventDetails;
