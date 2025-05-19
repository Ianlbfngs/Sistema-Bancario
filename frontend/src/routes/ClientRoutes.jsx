import React from 'react'
import MenuClient from "../client/template/MenuClient";
import Accounts from "../client/Accounts";
import Transfer from "../client/Transfer";
import Movements from "../client/Movements";
import AccountInformation from "../client/AccountInformation";


import { Navigate, Route, Routes } from 'react-router-dom'

export default function ClientRoutes() {
    
    const isAllowed = localStorage.getItem("rol") ==="client";
    if(!isAllowed){
        localStorage.setItem("rol","");
        return <Navigate to="/login" replace/>;
    }
    return (
        <Routes>
            <Route path='menu' element={<MenuClient />} />
            <Route path='accounts' element={<Accounts/>}/>
            <Route path='movements' element={< Movements />} />
            <Route path='transfer' element={< Transfer />} />
            <Route path='information' element={< AccountInformation />} />
        </Routes>
    )
}
