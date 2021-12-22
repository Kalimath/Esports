import React, { useEffect, useState} from 'react';
import SpelerService from '../../services/SpelerService';
import {NavLink, useNavigate} from 'react-router-dom';

function ListSpelersComponent () {

    const navigate = useNavigate();
    const [Spelers,setSpelers] = useState([]);
    const role = localStorage.getItem("role");


    useEffect(()=>{

        let abortController = new AbortController();
        async function fetchSpelers(){
            await SpelerService.getSpelers().then((res) =>{
                setSpelers(res.data);
            }).catch(error=>{
                if (error.response.status===404) {
                    navigate('/login');
                }
            });
        }
        fetchSpelers();

        return () => {
            abortController.abort();
        }
    },[navigate]);

        return (
            <div>
                <h1 className="titel">Spelers</h1>
                {role === "2" &&
                    <p>
                        <NavLink to='/spelers/create'>Create speler</NavLink>
                    </p>
                }
                <table className="table table-striped">
                    <thead className="table-dark">
                        <tr>
                            <th>Voornaam</th>
                            <th>Naam</th>
                            <th>email</th>
                            <th>Geboortedatum</th>
                            <th>Adres</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        {
                            Spelers.map((speler:any)=>
                                <tr key = {speler.id}>
                                    <td>{speler.voornaam}</td>
                                    <td>{speler.naam}</td>
                                    <td>{speler.email}</td>
                                    <td>{speler.geboortedatum}</td>
                                    <td>
                                       <pre>{speler.adres.straat} {speler.adres.huisnr}</pre> 
                                       <pre>{speler.adres.postcode} {speler.adres.gemeente}</pre>    
                                    </td>
                                    <td>
                                        {/*{role === "2" &&
                                            <NavLink style={{marginRight: "1em"}} to={`/spelers/update/${speler.id}`}>
                                                <button className="btn btn-outline-secondary btn-sm" >Update</button>
                                            </NavLink>
                                        }*/}

                                    </td>
                                </tr>
                            )
                        }
                    </tbody>

                </table>
                
            </div>
        );

}

export default ListSpelersComponent;