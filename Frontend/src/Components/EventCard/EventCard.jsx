import "./event.card.styles.css";
import EventIcon from "@mui/icons-material/Event";
import LocalOfferIcon from "@mui/icons-material/LocalOffer";
import PlaceIcon from "@mui/icons-material/Place";
import { useNavigate } from "react-router";

function EventCard({ id, imgUrl, title, dateTime, minimumPrice, location }) {
  const navigate = useNavigate();

  const handleButtonClick = () => {
    navigate(`/event/detail/${id}`);
  };

  return (
    <div className="event-card">
      <div className="event-img-container">
        <img src={imgUrl} alt="" className="event-img" />
      </div>
      <div className="event-data">
        <h3>{title}</h3>
        <div className="event-info">
          <div className="event-property">
            <h1>
              <EventIcon />
            </h1>
            <p>Fecha: {dateTime}</p>
          </div>
          {/* <div className="event-property">
            <h1>
              <LocalOfferIcon />
            </h1>
            <p>Desde: ${minimumPrice}</p>
          </div> */}
          <div className="event-property">
            <h1>
              <PlaceIcon />
            </h1>
            <p>Ubicación: {location}</p>
          </div>
        </div>
      </div>
      <button className="buy-button" onClick={handleButtonClick}>Comprar Entradas</button>
    </div>
  );
}
export default EventCard;
