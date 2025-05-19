import React from 'react'
import ClientListing from "../admin/clients/ClientListing";
import ClientAdd from "../admin/clients/ClientAdd";
import ClientDelete from "../admin/clients/ClientDelete";
import MenuAdmin from "../admin/template/MenuAdmin";
import AccountDelete from "../admin/accounts/AccountDelete";
import AccountListing from "../admin/accounts/AccountListing";
import AccountAdd from "../admin/accounts/AccountAdd";
import AccountEdit from "../admin/accounts/AccountEdit";
import AccountMovements from "../admin/accounts/AccountMovements";
import ClientEdit from "../admin/clients/ClientEdit";
import ClientCredentialEdit from "../admin/clients/ClientCredentialEdit";
import { Navigate, Route, Routes } from 'react-router-dom'

export default function AdminRoutes() {
    const isAllowed = localStorage.getItem("rol") === "admin";
    if (!isAllowed) {
        localStorage.setItem("rol", "");
        return <Navigate to="/login" replace />;
    }
    return (
        <Routes>
            <Route path='menu' element={<MenuAdmin />} />
            <Route path='client/add' element={<ClientAdd />} />
            <Route path='client/list' element={<ClientListing />} />
            <Route path='client/list/edit/:dniId' element={<ClientEdit />} />
            <Route path='client/list/edit/credential/:dniId' element={<ClientCredentialEdit />} />
            <Route path='client/delete' element={<ClientDelete />} />
            <Route path='account/add' element={<AccountAdd />} />
            <Route path='account/list' element={<AccountListing />} />
            <Route path='account/list/edit/:id' element={<AccountEdit />} />
            <Route path='account/list/movements/:id' element={<AccountMovements />} />
            <Route path='account/delete' element={<AccountDelete />} />
        </Routes>
    )
}
