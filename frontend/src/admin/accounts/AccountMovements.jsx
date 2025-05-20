import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'


export default function AccountMovements() {
  const { id } = useParams();
  const [apiOnline, setApiOnline] = useState(true);

  const [idFilter, setIdFilter] = useState('');
  const [typeFilter, setTypeFilter] = useState('');
  const [originIdFilter, setOriginIdFilter] = useState('');
  const [cbuTargetFilter, setCbuTargetFilter] = useState('');
  const [amountFilterHigh, setAmountFilterHigh] = useState('');
  const [amountFilterLow, setAmountFilterLow] = useState('');

  const [allMovements, setAllMovements] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [resOrigin, resTarget] = await Promise.all([
          axios.get(`http://localhost:8080/api/movements/origin/${id}`),
          axios.get(`http://localhost:8080/api/movements/target/${localStorage.getItem("cbu")}`)
        ]);

        const allMovements = [...resOrigin.data, ...resTarget.data];
        const allMovementsSorted = allMovements.sort((a, b) => new Date(b.date) - new Date(a.date));
        setAllMovements(allMovementsSorted);

      } catch (error) {
        if (error.code === "ERR_NETWORK" || error.response?.status === 500) setApiOnline(false);
        else console.error("Error al obtener movimientos:", error);
      }

    };

    fetchData();
  }, [id]);

  const filteredMovements = allMovements.filter(movement => {
    return (
      (idFilter ? movement.id === Number(idFilter) : true) &&
      (typeFilter ? movement.movementType.id === Number(typeFilter) : true) &&
      (originIdFilter ? movement.idAccount === Number(originIdFilter) : true) &&
      (cbuTargetFilter ? movement.cbu.includes(cbuTargetFilter) : true) &&
      (amountFilterHigh ? movement.amount >= Number(amountFilterHigh) : true) &&
      (amountFilterLow ? movement.amount <= Number(amountFilterLow) : true)

    );
  })

  const handleIdFilterChange = (e) => setIdFilter(e.target.value);
  const handleTypeFilterChange = (e) => setTypeFilter(e.target.value);
  const handleOriginIdFilterChange = (e) => setOriginIdFilter(e.target.value);
  const handleCbuTargetFilterChange = (e) => setCbuTargetFilter(e.target.value);
  const handleAmountHighFilterChange = (e) => setAmountFilterHigh(e.target.value);
  const handleAmountLowFilterChange = (e) => setAmountFilterLow(e.target.value);

  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 10;

  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentMovements = filteredMovements.slice(indexOfFirstItem, indexOfLastItem);

  const totalPages = Math.ceil((filteredMovements?.length || 0) / itemsPerPage);

  function renderMovements() {
    try {
      return currentMovements.map((movement, index) => (
        <tr key={index}>
          <th scope='row'>{movement.id}</th>
          <td>{movement.movementType.description}</td>
          <td>{movement.idAccount}</td>
          <td>{movement.cbu}</td>
          <td>{movement.date}</td>
          <td>{movement.detail}</td>
          <td style={{ color: movement.idAccount.toString() !== id ? "green" : "red" }}> {movement.idAccount.toString() !== id ? movement.amount : -movement.amount}</td>
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
        <h3>Listado de movimientos</h3>
      </div>
      <div className="d-flex justify-content-center mb-3">
        <input type='text' className='form-control' placeholder='Filtrar por ID' value={idFilter} onChange={handleIdFilterChange} style={{ width: '200px', marginRight: '10px' }} />
        <select className='form-select' value={typeFilter} onChange={handleTypeFilterChange} style={{ width: '200px', marginRight: '10px' }}>
          <option value="">Seleccionar tipo de movimiento</option>
          <option value={1}>Transferencia</option>
        </select>
        <input type='text' className='form-control' placeholder='Filtrar por ID de origen' value={originIdFilter} onChange={handleOriginIdFilterChange} style={{ width: '200px', marginRight: '10px' }} />
        <input type='text' className='form-control' placeholder='Filtrar por CBU de destino' value={cbuTargetFilter} maxLength={22} onChange={handleCbuTargetFilterChange} style={{ width: '200px', marginRight: '10px' }} />
        <input type='text' className='form-control' placeholder='Balance mayor a X' value={amountFilterHigh} onChange={handleAmountHighFilterChange} style={{ width: '200px', marginRight: '10px' }} />
        <input type='text' className='form-control' placeholder='Balance menor a X' value={amountFilterLow} onChange={handleAmountLowFilterChange} style={{ width: '200px', marginRight: '10px' }} />
      </div>
      <table className="table table-striped table-hover align-middle">
        <thead className='table-dark'>
          <tr>
            <th scope="col">Id</th>
            <th scope="col">Tipo de movimiento</th>
            <th scope="col">Id de cuenta origen</th>
            <th scope="col">CBU destino</th>
            <th scope="col">Fecha</th>
            <th scope="col">Detalle</th>
            <th scope="col">Monto</th>
          </tr>
        </thead>
        <tbody>
          {renderMovements()}
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
      {
        !apiOnline && (
          <div className='alert alert-danger text-center'>
            Error: No fue posible establecer conexion con el sistema bancario
          </div>
        )
      }
    </div>
  )
}
