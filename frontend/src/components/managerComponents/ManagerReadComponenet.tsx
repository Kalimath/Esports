import React, {useEffect, useState} from 'react';
import {NavLink, useNavigate, useParams} from "react-router-dom";
import ManagersService from "../../services/ManagersService";


function ManagerReadComponenet (){

    const params = useParams();
    const navigate = useNavigate();
    const [Manager, setManager] = useState({ id: null, naam: '', email: '' ,username:''});



    useEffect(() => {
        ManagersService.getManagerById(params.id).then((res) => {
            console.log(res.data);
            setManager(res.data);

        }).catch(error=>{
            if (error.response.status===404) {
                navigate('/login');
            }
        });
    }, [params,navigate]);
        return (
            <div>
                <h1>Mijn gegevens</h1>
                <p  style={{marginTop:"2em"}}>
                    <NavLink to={`/managers/update/${Manager.id}`}>Update mijn gegevens</NavLink>
                </p>
                <div style={{marginTop:"2em"}} className="row">
                    <div className="col-6">
                        <dl className="row">
                            <dt className="col-sm-4">
                                Naam
                            </dt>
                            <dd className="col-sm-8">
                                {Manager.naam}
                            </dd>
                            <dt className="col-sm-4">
                                Email
                            </dt>
                            <dd className="col-sm-8">
                                {Manager.email}
                            </dd>
                            <dt className="col-sm-4">
                                Username
                            </dt>
                            <dt className="col-sm-8">
                                {Manager.username}
                            </dt>

                        </dl>
                    </div>
                </div>
            </div>
        );

}

export default ManagerReadComponenet;