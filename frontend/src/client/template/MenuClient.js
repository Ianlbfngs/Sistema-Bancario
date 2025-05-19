import axios from 'axios';
import React, { useEffect } from 'react'
import { Link } from 'react-router-dom';

export default function Menu() {

  useEffect(() => {
    const fetchData = async () => {
      if (!localStorage.getItem("clientName")) {
        try {
          const result = await axios.get("http://localhost:8080/api/clients/" + localStorage.getItem("dni") + "/name");
          localStorage.setItem("clientName", result.data.name);

        } catch (error) {
          console.log(error);
        }
      }
    };

    fetchData();
  }, []);

  return (
    <div className='container'>
      <div className='container text-center' style={{ margin: "30px" }}>
        <h3>Bienvenido {localStorage.getItem("clientName")|| "cliente"}</h3>
      </div>
      <div className="row mb-3">
        <div className="col-md-6">
          <Link className='btn btn-primary btn-lg w-100 ' to="/client/accounts">Mis cuentas</Link>
        </div>
        <div className="col-md-6">
          <Link className='btn btn-primary btn-lg w-100 ' to="/client/transfer">Transferencias</Link>
        </div>
      </div>
      <div className="row mb-3">
        <div className="col-md-6">
          <Link className='btn btn-primary btn-lg w-100 ' to="/client/movements">Movimientos</Link>
        </div>
        <div className="col-md-6">
          <Link className='btn btn-primary btn-lg w-100 ' to="/client/information">Informacion personal</Link>
        </div>
      </div>
    </div>
  )
}
