import React, { useEffect, useState } from "react";
import { NavLink, useNavigate, useParams } from "react-router-dom";
import { useForm } from 'react-hook-form';
import ManagersService from "../../services/ManagersService";

function UpdateManagerComponent() {
    const params = useParams();
    const navigate = useNavigate();
    const [Manager, setManager] = useState({ id: null, naam: '', email: '',username:''});

    const { register, handleSubmit, formState: { errors }, reset, trigger } = useForm();

    useEffect(() => {
        ManagersService.getManagerById(params.id).then((res) => {
            setManager(res.data);
            reset(res.data);
        }).catch(error=>{
            if (error.response.status===404) {
                navigate('/login');
            }
        });
    }, [reset, params,navigate]);




    const onSubmit = (data: any) => {
        console.log(Manager)
        ManagersService.updateManager(params.id, data).then(() => {
            navigate(`/managers/read/${0}`);
        }).catch(error=>{
            if (error.response.status===404) {
                navigate('/login');
            }
        });
    }

    return (
        <div>
            <h1>Update jouw gegeven</h1>
            <form className="row g-3" onSubmit={handleSubmit(onSubmit)}>
                <div className="col-md-4">
                    <label className="form-label">Naam</label>
                    <input type="text" placeholder="Naam" defaultValue={Manager.naam} className={`form-control ${errors.naam && "invalid"}`}
                        {...register("naam", { required: "Naam is verplicht" })}
                        onKeyUp={() => {
                            trigger("naam");
                        }}
                    />
                    {errors.naam && (<small className="text-danger">{errors.naam.message}</small>)}
                </div>
                <div className="col-md-4">
                    <label className="form-label">Email</label>
                    <input type="text" className={`form-control ${errors.email && "invalid"}`} defaultValue={Manager.email} placeholder="voorbeel@gmail.com"
                        {...register("email", {
                            required: "Email is verplicht",
                            pattern: {
                                value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                                message: "Foutieve email adres",
                            }
                        })}
                        onKeyUp={() => {
                            trigger("email");
                        }}
                    />
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
                <div>
                    <div className="col-12">
                        <button style={{ marginRight: "1em" }} type="submit" className="btn btn-outline-success">Update</button>
                        <NavLink to='/wedstrijden'><button type="button" className="btn btn-outline-danger">Cancel</button></NavLink>
                    </div>
                </div>
            </form>

        </div>
    )

}

export default UpdateManagerComponent; 
