import axios from 'axios';
import React, { useEffect, useState } from 'react'

export default function Accounts() {

    const dni = localStorage.getItem("dni");

    const urlAccounts = "http://localhost:8080/api/accounts/dni/" + dni;

    const [apiOnline, setApiOnline] = useState(true);

    const [accounts, setAccounts] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const resultAccounts = await axios.get(urlAccounts);

                setAccounts(resultAccounts.data);
            } catch (error) {
                setApiOnline(false);
                console.error("Error: ", error);
            }
        };

        fetchData();
    }, [urlAccounts]);

    function renderAccounts() {
        try {
                return accounts.map((account, index) => (
                    <tr key={index}>
                        <th scope='row'>{account.id}</th>
                        <td>{account.accountType.description}</td>
                        <td>{account.dniClient}</td>
                        <td>{account.creationDate}</td>
                        <td>{account.cbu}</td>
                        <td>{account.balance}</td>
                    </tr>
                ));
        } catch (error) {
            console.error("Error al cargar cuentas:", error);
            return (
                <tr>
                    <td colSpan="6" style={{ color: 'red' }}>
                        Error al cargar las cuentas.
                    </td>
                </tr>
            );
        }
    }

    return (
        <div className='container-fluid'>
            <div className="container-fluid text-center" style={{ margin: "30px" }}>
                <h3>Listado de cuentas</h3>
            </div>
            <table className="table table-striped table-hover align-middle">
                <thead className='table-dark'>
                    <tr>
                        <th scope="col">Id</th>
                        <th scope="col">Tipo de cuenta</th>
                        <th scope="col">DNI del cliente</th>
                        <th scope="col">Fecha de creacion</th>
                        <th scope="col">CBU</th>
                        <th scope="col">Balance</th>
                    </tr>
                </thead>
                <tbody>
                    {accounts.length === 0 ? (
                        <tr>
                            <td colSpan="6">No hay cuentas disponibles.</td>
                        </tr>
                    ) : (
                        renderAccounts()
                    )}
                </tbody>
            </table>
            {!apiOnline && (
                <div className='alert alert-danger text-center'>
                    Error: No fue posible establecer conexion con el sistema bancario
                </div>
            )}
        </div>
    )
}