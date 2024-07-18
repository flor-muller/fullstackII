import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from './Components/Header/Header';
import Home from './Components/Home/Home';
import './App.css';
import Profile from './Pages/Profile/Profile';
import ProfileEdit from './Pages/ProfileEdit/ProfileEdit';
import Admin from './Pages/Admin/Admin';
import Footer from './Components/Footer/Footer';
import EventDetails from './Components/EventDetails/event.details';
import PrivateRoutes from './Components/PrivateRoutes/PrivateRoutes';


function App() {

  return (
    <>
      <Router>
      <Header />
        <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/profile" element={<Profile />} />
        {/* <Route path="/profile/edit" element={<ProfileEdit />} /> */}
        <Route path="/admin" element={<Admin />} />
        <Route path="/event/detail/:eventId" element={<EventDetails />} />
        {/* <Route path="/profile" element={<PrivateRoutes><Profile /></PrivateRoutes>} />
        <Route path="/profile/edit" element={<PrivateRoutes><ProfileEdit /></PrivateRoutes>} />
        <Route path="/admin" element={<PrivateRoutes><Admin /></PrivateRoutes>} /> */}
        </Routes>
      <Footer/>
    </Router>
    </>
  )
}

export default App

