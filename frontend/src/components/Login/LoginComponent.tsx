import React, {useState} from 'react';
import {NavLink, useNavigate} from "react-router-dom";
import LoginService from "../../services/LoginService";
import {useForm} from "react-hook-form";

function LoginComponent () {
    const navigate = useNavigate();
    const {register, handleSubmit,formState: {errors},trigger} = useForm()
    const [loginError, setLoginError] = useState("");


    const onSubmit = (data: any)=>{

        LoginService.login(data).then(res =>{
            console.log(res);
            if (res?.status === 200){
                LoginService.getRoleIngelogdeGebruiker().then((res)=>{
                    console.log(res);
                    localStorage.setItem('role',res.data);
                    navigate('/');
                });

            }else{
                setLoginError("Gebruikersnaam of passwoord kloppen niet")
            }
        });
    }

        return (
            <div >
                <h1>Login</h1>
                <form className="row g-3" onSubmit={handleSubmit(onSubmit)}>
                    <p className="text-danger">{loginError}</p>
                    <div style={{marginTop:"1em"}} className="row">
                        <div className=" col-md-6">
                            <label  className="form-label">Gebruikersnaam</label>
                            <input type="text" className="form-control" placeholder="Gebruikersnaam"
                                   {...register("username",{required: "Gebruikersnaam is verplicht"})}
                                   onKeyUp={() => {
                                       trigger("username");
                                   }}/>
                            {errors.username && (<small className="text-danger">{errors.username.message}</small>)}
                        </div>
                    </div>
                    <div style={{marginTop:"2em"}} className="row">
                        <div className="col-lg-6">
                            <label  className="form-label">Password</label>
                            <input type="password" className="form-control"
                                   placeholder="Passwoord"
                                   {...register("password",{required: "Passwoord is verplicht"})}
                                   onKeyUp={() => {
                                       trigger("password");
                                   }}/>
                            {errors.password && (<small className="text-danger">{errors.password.message}</small>)}
                        </div>
                    </div>

                    <div style={{marginLeft:"5px"}} className="col-sm-5">
                        <button type="submit" className="btn btn-primary">Sign in</button>
                    </div>

                    <NavLink  to='/spelers/create' className="dropdown-item" >Sign up als speler</NavLink>
                    <NavLink  to='/managers/create' className="dropdown-item" >Sign up als manager</NavLink>
                </form>

            </div>
        );
}
export default LoginComponent;