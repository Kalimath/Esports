import React, {useState} from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { useForm} from 'react-hook-form'; 
import ManagersService from "../../services/ManagersService";


function ManagerCreateComponent() {

    const navigate = useNavigate();
    const [errorRegister,setErrorRegister] = useState("");
    const {register, handleSubmit,formState: {errors},trigger} = useForm();
    const role = localStorage.getItem("role");
    const onSubmit = (data: any) => {
        ManagersService.createManager(data).then(res => {
            navigate('/');
        }).catch(err=>{
            console.log(err.response);
            if (err.response.status===400){
                setErrorRegister(err.response.data);
            }
        });

    }
    
    return (

        <div>
            {role === "1" &&
                <h1 className="text-danger">Geen toegang u bent een speler</h1>
            }
            {role !== "1" &&
                <><h1 className="titel">Create Manager</h1><p className="text-danger">{errorRegister}</p>
                    <form className="row g-3" onSubmit={handleSubmit(onSubmit)}>
                        <div className="col-md-6">
                            <label className="form-label">Naam</label>
                            <input type="text" placeholder="Naam" className={`form-control ${errors.naam && "invalid"}`}
                                   {...register("naam", {required: "Naam is verplicht"})}
                                   onKeyUp={() => {
                                       trigger("naam");
                                   }}/>
                            {errors.naam && (<small className="text-danger">{errors.naam.message}</small>)}
                        </div>
                        <div className="col-md-6">
                            <label className="form-label">Email</label>
                            <input type="text" className={`form-control ${errors.email && "invalid"}`}
                                   placeholder="voorbeel@gmail.com"
                                   {...register("email", {
                                       required: "Email is verplicht",
                                       pattern: {
                                           value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                                           message: "Foutieve email adres",
                                       }
                                   })}
                                   onKeyUp={() => {
                                       trigger("email");
                                   }}/>
                            {errors.email && (<small className="text-danger">{errors.email.message}</small>)}
                        </div>
                        <div className="col-6">
                            <label className="form-label">Gebruikersnaam</label>
                            <input type="text" className={`form-control ${errors.geboortedatum && "invalid"}`}
                                   placeholder="Gebruikersnaam"
                                   {...register("username", {required: "Gebruikersnaam is verplicht"})}
                                   onKeyUp={() => {
                                       trigger("username");
                                   }}/>
                            {errors.username && (<small className="text-danger">{errors.username.message}</small>)}
                        </div>
                        <div className="col-6">
                            <label className="form-label">Passwoord</label>
                            <input type="password" className={`form-control ${errors.straat && "invalid"}`}
                                   placeholder="Passwoord"
                                   {...register("password", {required: "Straat is verplicht"})}
                                   onKeyUp={() => {
                                       trigger("password");
                                   }}/>
                            {errors.password && (<small className="text-danger">{errors.password.message}</small>)}
                        </div>
                        <div>
                            <div className="col-12">
                                <button style={{marginRight: "1em"}} type="submit"
                                        className="btn btn-outline-success">Registreer
                                </button>
                                {role === "2" &&
                                    <NavLink to='/'>
                                        <button type="button" className="btn btn-outline-danger">Cancel</button>
                                    </NavLink>}
                                <NavLink to='/login'>
                                    <button type="button" className="btn btn-outline-danger">Cancel</button>
                                </NavLink>
                            </div>
                        </div>
                    </form>
                </>
            }

        </div>

    )

}

export default ManagerCreateComponent;