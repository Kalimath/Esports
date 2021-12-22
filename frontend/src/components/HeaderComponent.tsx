import React from 'react';
import {NavLink, useNavigate} from 'react-router-dom';
import LoginService from "../services/LoginService";




function HeaderComponent () {

    const navigate = useNavigate();
    const role = localStorage.getItem("role");


    const handleLogout = () => {
        console.log("clicked");
        localStorage.clear();
        LoginService.logout().then((res)=>{
            navigate("/login");
        }).catch(error =>{
            navigate("/login");
        });

    }

        return (

            
            <header>
                <nav className="navbar navbar-expand-sm navbar-toggleable-sm navbar-light bg-white border-bottom box-shadow mb-3">
                    <div className="container">
                        <div className = "navbar-collapse collapse d-sm-inline-flex justify-content-between" id="navbarNavDropdown">
                            <ul className="navbar-nav flex-grow-1">
                                <li className="nav-item">
                                    <NavLink className="nav-link text-dark" to='/'>Home |</NavLink>
                                </li>
                                {role === "1" &&
                                    <>
                                        <li className="nav-item">
                                            <NavLink className="nav-link text-dark" to='/spelers/read'>Mijn gegevens
                                                |</NavLink>
                                        </li>
                                        <li className="nav-item">
                                            <NavLink className="nav-link text-dark" to={`/wedstrijden/speler/${0}`}>Mijn match historiek
                                                |</NavLink>
                                        </li>
                                    </>
                                }
                                <li className="nav-item">
                                    <NavLink className="nav-link text-dark" to='/spelers'>Spelers |</NavLink>
                                </li>
                                <li className="nav-item">
                                    <NavLink className="nav-link text-dark" to='/teams'>Teams |</NavLink>
                                </li>
                                {role === "2" &&
                                    <>
                                        <li className="nav-item">
                                            <NavLink className="nav-link text-dark" to={`/managers/read/${0}`}>Mijn gegevens
                                                |</NavLink>
                                        </li>
                                        <li className="nav-item">
                                            <NavLink className="nav-link text-dark" to='/teams/manager'>Mijn teams
                                                |</NavLink>
                                        </li>
                                        <li className="nav-item">
                                            <NavLink className="nav-link text-dark" to='/wedstrijden'>Wedstrijden
                                                |</NavLink>
                                        </li>

                                    </>
                                }
                            </ul>
                            <ul className="navbar-nav flex-grow-1 ml-auto">
                                {(role !== "1" && role !== "2")  &&
                                    <li className="nav-item">
                                        <NavLink className="nav-link text-dark" to='/login'>Login</NavLink>
                                    </li>
                                }
                                {role ==="1" &&
                                    <li className="nav-item" onClick={handleLogout}>
                                        Logout
                                    </li>
                                }
                                {role === "2" &&
                                    <li className="nav-item" >
                                        <a className="nav-link text-dark" onClick={handleLogout}>Logout</a>
                                    </li>
                                }

                            </ul>
                        </div>
                    </div>
                </nav>
            </header>
        );

}

export default HeaderComponent;