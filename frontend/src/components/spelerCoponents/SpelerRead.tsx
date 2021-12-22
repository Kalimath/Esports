import React, {useEffect, useState} from 'react';
import SpelerService from "../../services/SpelerService";
import {NavLink, useNavigate} from "react-router-dom";

function SpelerRead () {

    const navigate = useNavigate();
    const [Speler,setSpeler]=useState({id: null,voornaam: '',naam:'', email:'', geboortedatum: '',username:'',pasword:'',
        adres:{id: null,straat:'',huisnr: '', postcode:'', gemeente:''}});

    useEffect(()=>{
        let abortController = new AbortController();
        async function fetchSpeler() {
            await SpelerService.getSpeler(0).then((res) => {
                setSpeler(res.data);
            }).catch(error => {
                if (error.response.status === 404) {
                    navigate('/login');
                }
            });
        }
        fetchSpeler();
        return () => {
            abortController.abort();
        }

    });

        return (
            <div>
                <h1>Mijn gegevens</h1>
                <p  style={{marginTop:"2em"}}>
                    <NavLink to={`/spelers/update/${Speler.id}`}>Update mijn gegevens</NavLink>
                </p>
                <div style={{marginTop:"2em"}} className="row">
                <div className="col-6">
                    <dl className="row">
                        <dt className="col-sm-4">
                            Voornaam
                        </dt>
                        <dd className="col-sm-8">
                            {Speler.voornaam}
                        </dd>
                        <dt className="col-sm-4">
                            Naam
                        </dt>
                        <dd className="col-sm-8">
                            {Speler.naam}
                        </dd>
                        <dt className="col-sm-4">
                            Email
                        </dt>
                        <dt className="col-sm-8">
                            {Speler.email}
                        </dt>

                    </dl>
                </div>
                <div className="col-6">
                    <dl className="row">
                        <dt className="col-sm-4">
                            Geboortedatum
                        </dt>
                        <dd className="col-sm-8">
                            {Speler.geboortedatum}
                        </dd>
                        <dt className="col-sm-4">
                            Gebruikersnaam
                        </dt>
                        <dd className="col-sm-8">
                            {Speler.username}
                        </dd>
                        <dt className="col-sm-4">
                            Adres
                        </dt>
                        <dd className="col-sm-8">
                            <pre>{Speler.adres.straat} {Speler.adres.huisnr}</pre>
                            <pre>{Speler.adres.postcode} {Speler.adres.gemeente}</pre>
                        </dd>
                    </dl>
                </div>
                </div>
            </div>
        );

}

export default SpelerRead;