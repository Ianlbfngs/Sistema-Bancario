import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom';

export default function ClientListing() {

  const urlClients = "http://localhost:8080/api/clients";
  const urlProvinces = "http://localhost:8080/api/provinces";

  const [apiOnline, setApiOnline] = useState(true);

  const [clients, setClients] = useState([]);
  const [provinces, setProvinces] = useState([]);

  const [nameFilter, setNameFilter] = useState('');
  const [surnameFilter, setSurnameFilter] = useState('');
  const [dniFilter, setDniFilter] = useState('');
  const [cuilFilter, setCUILFilter] = useState('');
  const [genderFilter, setGenderFilter] = useState('');
  const [provinceFilter, setProvinceFilter] = useState('');


  useEffect(() => {
    const fetchData = async () => {
      try {
        const resultClient = await axios.get(urlClients);
        const resultProvinces = await axios.get(urlProvinces);

        setClients(resultClient.data);
        setProvinces(resultProvinces.data);
      } catch (error) {
        setApiOnline(false);
        console.error("Error: ", error);
      }
    };

    fetchData();
  }, [urlClients, urlProvinces]);

  const filteredClients = Array.isArray(clients)
    ? clients.filter(client => {
      return (
        (nameFilter ? client.name.includes(nameFilter) : true) &&
        (surnameFilter ? client.surname.includes(surnameFilter) : true) &&
        (dniFilter ? client.dni.includes(dniFilter) : true) &&
        (cuilFilter ? client.cuil.includes(cuilFilter) : true) &&
        (genderFilter ? client.gender.id === Number(genderFilter) : true) &&
        (provinceFilter ? client.locality.province.id === Number(provinceFilter) : true)
      );
    })
    : [];

  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 10;

  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentClients = filteredClients.slice(indexOfFirstItem, indexOfLastItem);

  const totalPages = Math.ceil((filteredClients?.length || 0) / itemsPerPage);

  const handleNameFilterChange = (e) => setNameFilter(e.target.value);
  const handleSurnameFilterChange = (e) => setSurnameFilter(e.target.value);
  const handleDniFilterChange = (e) => setDniFilter(e.target.value);
  const handleCUILFilterChange = (e) => setCUILFilter(e.target.value);
  const handleGenderFilterChange = (e) => setGenderFilter(e.target.value);
  const handleProvinceFilterChange = (e) => setProvinceFilter(e.target.value);

  function renderClients() {
    try {
      return currentClients.map((client, indice) => (

        <tr key={indice}>
          <th scope='row'>{client.dni}</th>
          <td>{client.gender.description}</td>
          <td>{client.cuil}</td>
          <td>{client.surname}</td>
          <td>{client.name}</td>
          <td>{client.dob}</td>
          <td>{client.locality.province.description}</td>
          <td>{client.locality.description}</td>
          <td>{client.address}</td>
          <td>{client.email}</td>
          <td>{client.phoneNumber}</td>
          <td className='text-center'>
            <div>
              <Link to={`edit/${client.dni}`} className='btn btn-warning btn-sm me-3'>Editar cliente</Link>
              <Link to={`edit/credential/${client.dni}`} className='btn btn-warning btn-sm me-3'>Editar credencial</Link>
            </div>
          </td>
        </tr>
      ))
    } catch (error) {
      console.error("Error al cargar clientes:", error);
      return (
        <tr>
          <td colSpan="6" style={{ color: 'red' }}>Error al cargar los clientes</td>
        </tr>
      )
    }
  }

  return (
    <div className='container-fluid'>
      <div className="container-fluid text-center" style={{ margin: "30px" }}>
        <h3>Listado de clientes</h3>
      </div>
      <div className="d-flex justify-content-center mb-3">
        <input type='text' className='form-control' placeholder='Filtrar por DNI' value={dniFilter} maxLength={8} onChange={handleDniFilterChange} style={{ width: '200px', marginRight: '10px' }} />
        <input type='text' className='form-control' placeholder='Filtrar por CUIL' value={cuilFilter} maxLength={11} onChange={handleCUILFilterChange} style={{ width: '200px', marginRight: '10px' }} />
        <input type='text' className='form-control' placeholder='Filtrar por nombre' value={nameFilter} maxLength={44} onChange={handleNameFilterChange} style={{ width: '200px', marginRight: '10px' }} />
        <input type='text' className='form-control' placeholder='Filtrar por apellido' value={surnameFilter} maxLength={44} onChange={handleSurnameFilterChange} style={{ width: '200px', marginRight: '10px' }} />

        <select className='form-select' value={genderFilter} onChange={handleGenderFilterChange} style={{ width: '200px', marginRight: '10px' }}>
          <option value="">Seleccionar Género  </option>
          <option value="1">Hombre</option>
          <option value="2">Mujer</option>
          <option value="3">Otro</option>
        </select>
        <select className='form-select' value={provinceFilter} onChange={handleProvinceFilterChange} style={{ width: '200px', marginRight: '10px' }}>
          <option value="">Seleccionar Provincia</option>
          {Array.isArray(provinces)&&provinces.map((province, index) => (
            <option key={index} value={province.id}>{province.description}</option>
          ))}
        </select>
      </div>
      <table className="table table-striped table-hover align-middle">
        <thead className='table-dark'>
          <tr>
            <th scope="col">DNI</th>
            <th scope="col">Genero</th>
            <th scope="col">CUIL</th>
            <th scope="col">Apellido</th>
            <th scope="col">Nombre</th>
            <th scope="col">Fecha de nacimiento</th>
            <th scope="col">Provincia</th>
            <th scope="col">Localidad</th>
            <th scope="col">Direccion</th>
            <th scope="col">Email</th>
            <th scope="col">Telefono</th>
            <th scope="col">Editar</th>
          </tr>
        </thead>
        <tbody>
          {currentClients.length === 0 ? (
            <tr>
              <td colSpan="12" style={{ color: 'red' }}>No hay clientes.</td>
            </tr>
          ) : (renderClients()
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
