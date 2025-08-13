
import './App.css';
import Navbar from './components/common/Navbar.js';
import FooterComponent from './components/common/Footer.js';
import HomePage from './components/home/HomePage.js';
import { BrowserRouter,Routes,Route } from 'react-router-dom';
import AllRoomsPage from './components/booking_room/AllRoomsPage.js';
import FindBookingPage from './components/booking_room/FindBookingPage.js';
import RoomDetailsPage from './components/booking_room/RoomDetailsPage.js';

function App() {
  return (
    <BrowserRouter>
      <div className="App">
          <Navbar/>
          <div className='content'>
            <Routes>
            <Route exact path='/home' element={<HomePage/>}/>
            <Route exact path='/rooms' element={<AllRoomsPage/>}/>
            <Route path='/find-booking' element={<FindBookingPage/>}/>
            <Route path='/room-details-book/:roomId' element={<RoomDetailsPage/>}/>
            </Routes>
          </div>
          <FooterComponent/>
        </div>
    </BrowserRouter>
  );
}

export default App;
