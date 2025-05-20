import React from 'react'
export default function Menu() {
  return (
    <div className='container'>
      <div className='container text-center' style={{ margin: "30px" }}>
        <h3>Bienvenido administrador</h3>
      </div>
      <div className="row mb-3">
        <div className="col-md-6">
          <a className='btn btn-primary btn-lg  w-100  ' href='/admin/client/add'>Agregar clientes</a>
        </div>
        <div className="col-md-6">
          <a className='btn btn-primary btn-lg    w-100' href='/admin/account/add'>Agregar cuentas</a>
        </div>
      </div>
      <div className="row mb-3">
        <div className="col-md-6">
          <a className='btn btn-primary btn-lg   w-100' href='/admin/client/list'>Listar clientes</a>
        </div>
        <div className="col-md-6">
          <a className='btn btn-primary btn-lg   w-100' href='/admin/account/list'>Listar cuentas</a>
        </div>
      </div>
      <div className="row mb-3">
        <div className="col-md-6">
          <a className='btn btn-primary btn-lg   w-100' href='/admin/client/delete'>Eliminar clientes</a>
        </div>
        <div className="col-md-6">
          <a className='btn btn-primary btn-lg   w-100' href='/admin/account/delete'>Eliminar cuentas</a>
        </div>
      </div>
    </div>
  )
}
