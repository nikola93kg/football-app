import React from 'react'
import { Routes, Route } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import HomePage from "./pages/HomePage";
import TeamList from './components/team/TeamList';
import AddTeam from "./components/team/AddTeam";
import Navbar from './components/Navbar';
import "./styles/App.css"
import EditTeam from './components/team/EditTeam';

function App() {
  return (
    <div className='main-container'>
      <Navbar />
      <ToastContainer />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/teams" element={<TeamList />} />
        <Route path="/add-team" element={<AddTeam />} />
        <Route path='/edit-team/:id' element={<EditTeam />} />
      </Routes>
    </div>
  )
}

export default App