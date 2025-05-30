import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import '../src/index.css'
import HotelApp from "./HotelApp.jsx";

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <HotelApp />
  </StrictMode>,
)
