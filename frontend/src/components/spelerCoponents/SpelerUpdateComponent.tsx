import React, {useEffect, useState} from 'react';
import {NavLink, useParams} from 'react-router-dom';
import { useNavigate} from "react-router-dom";
import { useForm } from 'react-hook-form';
import SpelerService from '../../services/SpelerService';


 function SpelerUpdateComponent () {
    const params = useParams();
    const navigate = useNavigate();

    const [Speler,setSpeler]=useState({id: null, voornaam: '',naam:'', email:'', geboortedatum: '',username:'',
            adres:{id: null,straat:'',huisnr: '', postcode:'', gemeente:''}});

    const role = localStorage.getItem("role");
     const {register, handleSubmit,formState: {errors},trigger,reset} = useForm();

    useEffect(()=>{
        SpelerService.getSpeler(params.id).then((res)=>{

            console.log(res.data);
            setSpeler(res.data);
            reset(res.data);
        }).catch(error=>{
            console.log(error.response.status);
            if (error.response.status===404) {
                navigate('/login');
            }
        });
    },[reset,params]);
    


    const onSubmit = (data: any)=>{
        
        SpelerService.updateSpeler(data , Speler.id).then( () =>{
            if (role === "1"){
                navigate('/spelers/read');
            }else{
                navigate('/spelers');
            }
        });     
    }
    return (
        <div>
            <h1 className="titel">Update speler</h1>
            <form className="row g-3 "  onSubmit={handleSubmit(onSubmit)}>
                <div className="col-md-6">
                    <label  className="form-label">Voornaam</label>
                    <input   type="text"   placeholder="Voornaam" defaultValue={Speler.voornaam} className={`form-control ${errors.voornaam && "invalid"}`}
                    {...register("voornaam",{required: "Voornaam is verplicht"})}
                    onKeyUp={() => {
                        trigger("voornaam");
                      }}
                    />
                    {errors.voornaam && (<small className="text-danger">{(errors.voornaam as any).message}</small>)}
                </div>
                <div className="col-md-6">
                    <label  className="form-label">Naam</label>
                    <input  type="text" defaultValue={Speler.naam} className={`form-control ${errors.naam && "invalid"}`} placeholder="Naam"
                    {...register("naam",{required: "Naam is verplicht"})} 
                    
                    onKeyUp={() => {
                        trigger("naam");
                      }}
                    />
                     {errors.naam && (<small className="text-danger">{(errors.naam as any).message}</small>)} 
                </div>

                <div className="col-6">
                    <label className="form-label">Geboortedatum</label>
                    <input  type="date" defaultValue={Speler.geboortedatum} className={`form-control ${errors.geboortedatum && "invalid"}`}
                     {...register("geboortedatum",{required: "Geboortedatum is verplicht"})}
                    
                     onKeyUp={() => {
                        trigger("geboortedatum");
                      }}
                     />
                    {errors.geboortedatum && (<small className="text-danger">{(errors.geboortedatum as any).message}</small>)}
                </div>
                <div className="col-6">
                    <label  className="form-label">Straat</label>
                    <input  type="text" defaultValue={Speler.adres.straat} className={`form-control ${errors.straat && "invalid"}`} placeholder="Straat"
                    {...register("straat",{required: "Straat is verplicht"})}
                  
                    onKeyUp={() => {
                        trigger("straat");
                      }}
                      />
                    {errors.straat && (<small className="text-danger">{(errors.straat as any).message}</small>)}
                </div>
                <div className="col-md-4">
                    <label  className="form-label">Huisnr</label>
                    <input  type="text" defaultValue={Speler.adres.huisnr} className={`form-control ${errors.huisnummer && "invalid"}`} placeholder="Huisnummer"
                    {...register("huisnummer",{required: "Huisnr is verplicht"})}
                   
                    onKeyUp={() => {
                        trigger("huisnummer");
                      }}
                    />
                    {errors.huisnummer && (<small className="text-danger">{(errors.huisnummer as any).message}</small>)}
                </div>
                <div className="col-md-4">
                    <label  className="form-label">Postcode</label>
                    <input {...register("postcode",{required: "Postcode is verplicht"})} type="text" defaultValue={Speler.adres.postcode} className={`form-control ${errors.postcode && "invalid"}`} placeholder="Postcode"
                    {...register("postcode",{required: "Postcode is verplicht"})}
                   
                    onKeyUp={() => {
                        trigger("postcode");
                      }}
                    />
                    {errors.postcode && (<small className="text-danger">{(errors.postcode as any).message}</small>)}
                </div>
                <div className="col-md-4">
                    <label  className="form-label">Gemeente</label>
                    <input  type="text" defaultValue={Speler.adres.gemeente} className={`form-control ${errors.gemeente && "invalid"}`} placeholder="Gemeente"
                    {...register("gemeente",{required: "Gemeente is verplicht"})}
                   
                    onKeyUp={() => {
                        trigger("gemeente");
                      }}
                    />
                    {errors.gemeente && (<small className="text-danger">{(errors.gemeente as any).message}</small>)}
                </div>
                <div className="col-6">
                    <label className="form-label">Gebruikersnaam</label>
                    <input type="text" defaultValue={Speler.username} className={`form-control ${errors.geboortedatum && "invalid"}`}
                           placeholder="Gebruikersnaam"
                           {...register("username", {required: "Gebruikersnaam is verplicht"})}
                           onKeyUp={() => {
                               trigger("username");
                           }}/>
                    {errors.username && (<small className="text-danger">{errors.username.message}</small>)}
                </div>
                <div className="col-6">
                    <label className="form-label">Email</label>
                    <input  type="text" defaultValue={Speler.email} className={`form-control ${errors.email && "invalid"}`} placeholder="voorbeel@gmail.com"
                            {...register("email",{required: "Email is verplicht",
                                pattern: {
                                    value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                                    message: "Foutieve email adres",
                                }})}

                            onKeyUp={() => {
                                trigger("email");
                            }}
                    />
                    {errors.email && (<small className="text-danger">{(errors.email as any)?.message}</small>)}
                </div>

                <div className="col-12">
                    <button style={{marginRight: "1em" }} type="submit" className="btn btn-outline-success">Update</button>
                    {role==="1"&&
                        <NavLink to='/spelers/read'><button type="button" className="btn btn-outline-danger">Cancel</button></NavLink>
                    }
                    {role !== "1" &&
                        <NavLink to='/spelers'><button type="button" className="btn btn-outline-danger">Cancel</button></NavLink>
                    }
                </div>
            </form>
        </div>
    );
}
export default SpelerUpdateComponent;