import { useState, useEffect } from "react";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import "./register.styles.css";

function Register(){
    const [userData, setUserData] = useState({
        username: '',
        password: '',
        rePassword: '',
        firstName: '',
        lastName: '',
        email: ''
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setUserData({
            ...userData,
            [name]: value
        });
    };

    const [error, setError] = useState(null);

    return (
        <div className="register-container">
          <div className="register-form">
            <form /*onSubmit={handleSubmit}*/ >
              <h2>Registrarse</h2>
              {error && <div className="error">{error}</div>}
              <div className="form-container">
                <TextField
                  id="outlined-basic"
                  label="Nombre"
                  variant="outlined"
                  type="text"
                  name="firstName"
                  value={userData.firstName}
                  onChange={handleChange}
                  required
                />
                <TextField
                  id="outlined-basic"
                  label="Apellido"
                  variant="outlined"
                  type="text"
                  name="lastName"
                  value={userData.lastName}
                  onChange={handleChange}
                  required
                />
                <TextField
                  id="outlined-basic"
                  label="Email"
                  variant="outlined"
                  type="email"
                  name="email"
                  value={userData.email}
                  onChange={handleChange}
                  required
                />
                <TextField
                  id="outlined-basic"
                  label="Usuario"
                  variant="outlined"
                  type="text"
                  name="username"
                  value={userData.username}
                  onChange={handleChange}
                  required
                />
                <TextField
                  id="outlined-basic"
                  label="Contraseña"
                  variant="outlined"
                  type="password"
                  name="password"
                  value={userData.password}
                  onChange={handleChange}
                  required
                />
                <TextField
                  id="outlined-basic"
                  label="Repetir Contraseña"
                  variant="outlined"
                  type="password"
                  name="rePassword"
                  value={userData.rePassword}
                  onChange={handleChange}
                  required
                />
                <Button variant="contained" type="submit">
                  Ingresar
                </Button>
              </div>
            </form>
          </div>
        </div>
      );
}

export default Register