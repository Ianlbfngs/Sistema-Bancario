import axios from 'axios';
import React, { useEffect, useState } from 'react'


export default function Movements() {
  const dni = localStorage.getItem("dni");

  const [apiOnline, setApiOnline] = useState(true);

  const [typeFilter, setTypeFilter] = useState('');
  const [cbuTargetFilter, setCbuTargetFilter] = useState('');
  const [amountFilterHigh, setAmountFilterHigh] = useState('');
  const [amountFilterLow, setAmountFilterLow] = useState('');

  const [allMovements, setAllMovements] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const resultAccounts = await axios.get("http://localhost:8080/api/accounts/dni/" + dni);

        const movementsPromises = resultAccounts.data.flatMap(account => [
          { type: "ENVIADO", promise: axios.get(`http://localhost:8080/api/movements/origin/${account.id}`) },
          { type: "RECIBIDO", promise: axios.get(`http://localhost:8080/api/movements/target/${account.cbu}`) }
        ]);

        const results = await Promise.all(movementsPromises.map(r => r.promise));
        const allMovements = results.flatMap((res, index) => {
          const tipo = movementsPromises[index].type;

          if (!Array.isArray(res.data)) return [];

          return res.data.map(mov => ({
            ...mov,
            amount: tipo === "ENVIADO" ? -Math.abs(mov.amount) : Math.abs(mov.amount),
            tipo
          }));
        });

        setAllMovements(allMovements);

      } catch (error) {
        if (error.code === "ERR_NETWORK" || error.response?.status === 500) setApiOnline(false);
        else console.error("Error al obtener movimientos:", error);
      }

    };

    fetchData();
  }, [dni]);

  const filteredMovements = allMovements.filter(movement => {
    return (
      (typeFilter ? movement.movementType.id === Number(typeFilter) : true) &&
      (cbuTargetFilter ? movement.cbu.includes(cbuTargetFilter) : true) &&
      (amountFilterHigh ? movement.amount >= Number(amountFilterHigh) : true) &&
      (amountFilterLow ? movement.amount <= Number(amountFilterLow) : true)
    );
  })


  const handleTypeFilterChange = (e) => setTypeFilter(e.target.value);
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
          <td style={{ color: movement.amount > 0 ? "green" : "red" }}>{movement.amount}</td>
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
      )
    }
  }

  return (
    <div className='container-fluid'>
      <div className="container-fluid text-center" style={{ margin: "30px" }}>
        <h3>Listado de movimientos</h3>
      </div>
      <div className="d-flex justify-content-center mb-3">
        <select className='form-select' value={typeFilter} onChange={handleTypeFilterChange} style={{ width: '200px', marginRight: '10px' }}>
          <option value="">Seleccionar tipo de movimiento</option>
          <option value={1}>Transferencia</option>
        </select>
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
          {currentMovements.length === 0 ? (
            <tr>
              <td colSpan="7" style={{ color: 'red' }}>No hay movimientos.</td>
            </tr>
          ) : (renderMovements()
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
