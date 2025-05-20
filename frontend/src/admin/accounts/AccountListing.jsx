import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom';

export default function AccountListing() {

  const navigate = useNavigate();



  const urlAccounts = "http://localhost:8080/api/accounts";
  const urlTypes = "http://localhost:8080/api/accounts/types";

  const [apiOnline, setApiOnline] = useState(true);


  const [accounts, setAccounts] = useState([]);
  const [accountTypes, setAccountTypes] = useState([]);


  const [typeFilter, setTypeFilter] = useState('');
  const [dniFilter, setDniFilter] = useState('');
  const [cbuFilter, setCBUFilter] = useState('');
  const [balanceFilterHigh, setBalanceFilterHigh] = useState('');
  const [balanceFilterLow, setBalanceFilterLow] = useState('');


  useEffect(() => {
    const fetchData = async () => {
      try {
        const resultAccounts = await axios.get(urlAccounts);
        const resultAccountTypes = await axios.get(urlTypes);

        setAccountTypes(resultAccountTypes.data);
        setAccounts(resultAccounts.data);
      } catch (error) {
        if (error.code === "ERR_NETWORK" || error.response?.status === 500) setApiOnline(false);
        else console.error("Error al obtener movimientos:", error);
      }
    };

    fetchData();
  }, [urlAccounts, urlTypes]);

  const filteredAccounts = Array.isArray(accounts) ? accounts.filter(account => {
    return (
      (typeFilter ? account.accountType.id === Number(typeFilter) : true) &&
      (dniFilter ? account.dniClient.includes(dniFilter) : true) &&
      (cbuFilter ? account.cbu.includes(cbuFilter) : true) &&
      (balanceFilterHigh ? account.balance >= Number(balanceFilterHigh) : true) &&
      (balanceFilterLow ? account.balance <= Number(balanceFilterLow) : true)
    );
  })
    : [];

  const handleTypeFilterChange = (e) => setTypeFilter(e.target.value);
  const handleDNIFilterChange = (e) => setDniFilter(e.target.value);
  const handleCBUFilterChange = (e) => setCBUFilter(e.target.value);
  const handleBalanceHighFilterChange = (e) => setBalanceFilterHigh(e.target.value);
  const handleBalanceLowFilterChange = (e) => setBalanceFilterLow(e.target.value);


  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 10;

  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentAccounts = filteredAccounts.slice(indexOfFirstItem, indexOfLastItem);

  const totalPages = Math.ceil((filteredAccounts?.length || 0) / itemsPerPage);

  function renderAccounts() {
    try {
      return currentAccounts.map((account, index) => (
        <tr key={index}>
          <th scope='row'>{account.id}</th>
          <td>{account.accountType.description}</td>
          <td>{account.dniClient}</td>
          <td>{account.creationDate}</td>
          <td>{account.cbu}</td>
          <td>{account.balance}</td>
          <td> <Link to={`edit/${account.id}`} className='btn btn-warning btn-sm'>Editar cuenta</Link></td>
          <td><button className='btn btn-warning btn-sm' onClick={() => { localStorage.setItem("cbu", account.cbu); navigate(`movements/${account.id}`); }}>Ver movimientos</button></td>
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
      <div className="d-flex justify-content-center mb-3">
        <input type='text' className='form-control' placeholder='Filtrar por DNI' value={dniFilter} maxLength={8} onChange={handleDNIFilterChange} style={{ width: '200px', marginRight: '10px' }} />
        <input type='text' className='form-control' placeholder='Filtrar por CBU' value={cbuFilter} maxLength={22} onChange={handleCBUFilterChange} style={{ width: '200px', marginRight: '10px' }} />
        <input type='text' className='form-control' placeholder='Balance mayor a X' value={balanceFilterHigh} onChange={handleBalanceHighFilterChange} style={{ width: '200px', marginRight: '10px' }} />
        <input type='text' className='form-control' placeholder='Balance menor a X' value={balanceFilterLow} onChange={handleBalanceLowFilterChange} style={{ width: '200px', marginRight: '10px' }} />

        <select className='form-select' value={typeFilter} onChange={handleTypeFilterChange} style={{ width: '200px', marginRight: '10px' }}>
          <option value="">Seleccionar tipo de cuenta</option>
          {Array.isArray(accountTypes) &&accountTypes.map((type, index) => (
            <option key={index} value={type.id}>{type.description}</option>
          ))}
        </select>
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
            <th scope="col">Editar</th>
            <th scope="col">Movimientos</th>
          </tr>
        </thead>
        <tbody>
          {currentAccounts.length === 0 ? (
            <tr>
              <td colSpan="8" style={{ color: 'red' }}>No hay cuentas.</td>
            </tr>
          ) : (renderAccounts()
          )}
        </tbody>
      </table>
      <div>
        <nav className="d-flex justify-content-center">
          <ul className="pagination">
            {Array.from({ length: totalPages }, (_, i) => (
              <li key={i + 1} className={`page-item ${currentPage === i + 1 ? 'active' : ''}`}>
                <button className="page-link" onClick={() => setCurrentPage(i + 1)}>{i + 1}</button>
              </li>
            ))}
          </ul>
        </nav>
      </div>
      {!apiOnline && (
        <div className='alert alert-danger text-center'>
          Error: No fue posible establecer conexion con el sistema bancario
        </div>
      )}
    </div>
  )
}
