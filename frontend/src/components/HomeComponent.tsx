import React, {useEffect} from 'react';
import LoginService from "../services/LoginService";
import {useNavigate} from "react-router-dom";





function HomeComponent (){

    const navigate = useNavigate();

    useEffect(()=>{
        LoginService.getRoleIngelogdeGebruiker().then((res)=>{

        }).catch(error=>{
            if (error.response.status === 404){
                navigate('/login');
            }
        });
    });
    return (
            <div className="text-center ">
                <img src="/logo_large.png" className="img-fluid centered" alt="logo site">
                </img>
                
            </div>
        );

}

export default HomeComponent;