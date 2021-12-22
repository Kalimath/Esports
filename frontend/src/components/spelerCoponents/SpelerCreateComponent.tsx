import React, {useState} from 'react';
import { NavLink } from 'react-router-dom';
import SpelerService from '../../services/SpelerService';
import { useNavigate} from "react-router-dom";
import { useForm } from 'react-hook-form';




 function SpelerCreateComponent () {
    
    const navigate = useNavigate();
    const [errorRegister,setErrorRigister] = useState("");
    const role = localStorage.getItem("role");

   
    const {register, handleSubmit,formState: {errors},trigger} = useForm()
   

    const onSubmit = (data: any)=>{
            SpelerService.createSpeler(data).then((res) =>{
                navigate('/spelers');

            }).catch((err)=>{
                console.log(err.response);
                if (err.response.status===400){
                    setErrorRigister(err.response.data);
                }
            });
    }
    
    return (

        <div>
            {role ==="1" &&
               <h1 className="text-danger">Geen toegang u bent een speler</h1>
            }
            {role !=="1" &&
                <><h1 className="titel">Create speler</h1><p className="text-danger">{errorRegister}</p>
                    <form className="row g-3 " onSubmit={handleSubmit(onSubmit)}>
                        <div className="col-md-4">
                            <label className="form-label">Voornaam</label>
                            <input type="text" placeholder="Voornaam"
                                   className={`form-control ${errors.voornaam && "invalid"}`}
                                   {...register("voornaam", {required: "Voornaam is verplicht"})}
                                   onKeyUp={() => {
                                       trigger("voornaam");
                                   }}/>
                            {errors.voornaam && (<small className="text-danger">{errors.voornaam.message}</small>)}
                        </div>
                        <div className="col-md-4">
                            <label className="form-label">Naam</label>
                            <input type="text" className={`form-control ${errors.naam && "invalid"}`} placeholder="Naam"
                                   {...register("naam", {required: "Naam is verplicht"})}
                                   onKeyUp={() => {
                                       trigger("naam");
                                   }}/>
                            {errors.naam && (<small className="text-danger">{errors.naam.message}</small>)}
                        </div>
                        <div className="col-md-4">
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
                            <label className="form-label">Geboortedatum</label>
                            <input type="date" className={`form-control ${errors.geboortedatum && "invalid"}`}
                                   {...register("geboortedatum", {required: "Geboortedatum is verplicht"})}
                                   onKeyUp={() => {
                                       trigger("geboortedatum");
                                   }}/>
                            {errors.geboortedatum && (
                                <small className="text-danger">{errors.geboortedatum.message}</small>)}
                        </div>
                        <div className="col-6">
                            <label className="form-label">Straat</label>
                            <input type="text" className={`form-control ${errors.straat && "invalid"}`}
                                   placeholder="Straat"
                                   {...register("straat", {required: "Straat is verplicht"})}
                                   onKeyUp={() => {
                                       trigger("straat");
                                   }}/>
                            {errors.straat && (<small className="text-danger">{errors.straat.message}</small>)}
                        </div>
                        <div className="col-md-4">
                            <label className="form-label">Huisnr</label>
                            <input type="text" className={`form-control ${errors.huisnummer && "invalid"}`}
                                   placeholder="Huisnummer"
                                   {...register("huisnummer", {required: "Huisnr is verplicht"})}
                                   onKeyUp={() => {
                                       trigger("huisnummer");
                                   }}/>
                            {errors.huisnummer && (<small className="text-danger">{errors.huisnummer.message}</small>)}
                        </div>
                        <div className="col-md-4">
                            <label className="form-label">Postcode</label>
                            <input {...register("postcode", {required: "Postcode is verplicht"})} type="text"
                                   className={`form-control ${errors.postcode && "invalid"}`} placeholder="Postcode"
                                   {...register("postcode", {required: "Postcode is verplicht"})}
                                   onKeyUp={() => {
                                       trigger("postcode");
                                   }}/>
                            {errors.postcode && (<small className="text-danger">{errors.postcode.message}</small>)}
                        </div>
                        <div className="col-md-4">
                            <label className="form-label">Gemeente</label>
                            <input type="text" className={`form-control ${errors.gemeente && "invalid"}`}
                                   placeholder="Gemeente"
                                   {...register("gemeente", {required: "Gemeente is verplicht"})}
                                   onKeyUp={() => {
                                       trigger("gemeente");
                                   }}/>
                            {errors.gemeente && (<small className="text-danger">{errors.gemeente.message}</small>)}
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
                        <div className="col-12">
                            <button style={{marginRight: "1em"}} type="submit"
                                    className="btn btn-outline-success">Registreer
                            </button>
                            {role ==="2" &&
                                <NavLink to='/spelers'>
                                <button type="button" className="btn btn-outline-danger">Cancel</button>
                                </NavLink>
                            }
                            {role !=="2" &&
                                <NavLink to='/login'>
                                    <button type="button" className="btn btn-outline-danger">Cancel</button>
                                </NavLink>
                            }
                        </div>
                    </form>
                </>
            }

        </div>
    );
}
export default SpelerCreateComponent;

