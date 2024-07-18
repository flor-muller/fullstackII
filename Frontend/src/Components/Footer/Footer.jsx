import "./footer.styles.css";
import InstagramIcon from '@mui/icons-material/Instagram';
import FacebookOutlinedIcon from '@mui/icons-material/FacebookOutlined';
import TwitterIcon from '@mui/icons-material/Twitter';
import YouTubeIcon from '@mui/icons-material/YouTube';

function Footer() {
  return (
    <div className="footer-container">
      <div className="footer-info1">
        <p style={{ fontWeight: "bold"}}>TickeTrek</p>
        <p>info@ticketrek.com</p>
        <p style={{ fontWeight: "bold"}}>Atención al cliente:</p>
        <p>+57 3124168770</p>
      </div>
      <div className="footer-info1">
        <br/>
        <p>Preguntas Frecuentes</p>
        <p>Políticas de Privacidad</p>
        <p>Términos y Condiciones</p>
      </div>
      <div className="footer-info1">
        <br/>
        <p>Políticas de Reembolso</p>
        <p>Puntos de Venta</p>
        <p>Asistencia TickeTrek</p>
      </div>
      <div className="footer-info1">
        <p style={{ fontWeight: "bold"}}>Síguenos en:</p>
        <div className="icon-container">
        <InstagramIcon fontSize="large"></InstagramIcon>
        <FacebookOutlinedIcon fontSize="large"></FacebookOutlinedIcon>
        <TwitterIcon fontSize="large"></TwitterIcon>
        <YouTubeIcon fontSize="large"></YouTubeIcon>
        </div>
        <p className="copyright">© 2024 TickeTrek - Digital House</p>
      </div>
    </div>
  );
}

export default Footer;
