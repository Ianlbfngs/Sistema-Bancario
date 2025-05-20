import React from 'react'
import { Link, useLocation, useNavigate } from 'react-router-dom';

export default function Navigation() {
  const location = useLocation();
  const navigate = useNavigate();

  if (location.pathname === "/login") {
    return null;
  }
  const rol = localStorage.getItem("rol");
  const handleLogout = () => {
    localStorage.removeItem("rol");
    localStorage.removeItem("dni");
    localStorage.removeItem("clientName");
    navigate("/login");
  };
  return (
    <div className='container-fluid'>
      <nav className="navbar navbar-expand-lg navbar-dark bg-primary">
        <div className="container-fluid">
          <a className="navbar-brand" href={rol === 'admin' ? "/admin/menu" : "/client/menu"}>
            Sistema bancario
          </a>

          <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span className="navbar-toggler-icon"></span>
          </button>

          <div className="collapse navbar-collapse" id="navbarNav">
            <ul className="navbar-nav">
              {
                rol === "admin" ? (
                  <>
                    <li className="nav-item">
                      <Link className="nav-link active" to="/admin/menu">Inicio</Link>
                    </li>
                    <li className="nav-item">
                      <Link className="nav-link active" to="/admin/client/add">Agregar cliente</Link>
                    </li>
                    <li className="nav-item">
                      <Link className="nav-link active" to="/admin/client/list">Listar clientes</Link>
                    </li>
                    <li className="nav-item">
                      <Link className="nav-link active" to="/admin/client/delete">Eliminar cliente</Link>
                    </li>
                    <li className="nav-item">
                      <Link className="nav-link active" to="/admin/account/add">Agregar cuenta</Link>
                    </li>
                    <li className="nav-item">
                      <Link className="nav-link active" to="/admin/account/list">Listar cuentas</Link>
                    </li>
                    <li className="nav-item">
                      <Link className="nav-link active" to="/admin/account/delete">Eliminar cuenta</Link>
                    </li>
                  </>
                ) : (
                  <>
                    <li className="nav-item">
                      <Link className="nav-link active" to="/client/menu">Inicio</Link>
                    </li>
                    <li className="nav-item">
                      <Link className="nav-link active" to="/client/accounts">Mis cuentas</Link>
                    </li>
                    <li className="nav-item">
                      <Link className="nav-link active" to="/client/transfer">Transferencias</Link>
                    </li>
                    <li className="nav-item">
                      <Link className="nav-link active" to="/client/movements">Movimientos</Link>
                    </li>
                    <li className="nav-item">
                      <Link className="nav-link active" to="/client/information">Informacion de cuenta</Link>
                    </li>
                  </>
                )
              }
            </ul>
          </div>

          <button className="nav-link active btn btn-link text-white" onClick={handleLogout}>Cerrar sesion</button>
        </div>
      </nav>
    </div>
  );
}
