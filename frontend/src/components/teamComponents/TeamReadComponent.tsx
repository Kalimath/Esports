import React, { useEffect,useState } from 'react';
import {NavLink, useNavigate, useParams} from 'react-router-dom';
import SpelerService from '../../services/SpelerService';
import TeamService from '../../services/TeamService';




function TeamReadComponent (){
    const params = useParams();
    const navigate = useNavigate();
    const [dataObject,setData]= useState({id: null,naam: ''});
    const [dataObjectManager, setManager] = useState({id: null, naam: '', email: ''});
    const [actieveSpelers,setActieveSpelers] = useState([]);
    const [reserveSpelers,setReserveSpelers] = useState([]);
    const role = localStorage.getItem("role");
    const [isMijnTeam,setIsMijnTeam] = useState(false);

    useEffect(()=>{
        let abortController = new AbortController();
        async function fetchApiData(){
            await TeamService.getTeam(params.id).then((res) =>{
                setData(res.data);
                if(res.data.manager != null){
                    setManager(res.data.manager)
                }

            }).catch(error=>{
                if (error.response.status===404) {
                    navigate('/login');
                }
            });
            await SpelerService.getActieveSpelersVanEenTeam(params.id).then((res) =>{

                setActieveSpelers(res.data);
            }).catch(error=>{
                if (error.response.status===404) {
                    navigate('/login');
                }
            });
            await SpelerService.getReserveSpelersVanEenTeam(params.id).then((res) =>{

                setReserveSpelers(res.data);
            }).catch(error=>{
                if (error.response.status===404) {
                    navigate('/login');
                }
            });
            if (role === "2"){
                await TeamService.teamBehoortTotManager(params.id).then((res) =>{
                    console.log(res.data);
                    setIsMijnTeam(res.data);
                }).catch(error=>{
                    if (error.response.status === 403){
                        console.log(error.response.status);
                    }
                });
            }

        }
        fetchApiData();
        return () => {
            abortController.abort();
        }
    
    },[params,navigate]);

    const handleDemote = (actieveSpeler:any) =>{
        TeamService.demoteSpeler(actieveSpeler.id).then(()=>{
            window.location.reload();
        }).catch(error=>{
            if (error.response.status===404) {
                navigate('/login');
            }
        });

    }
    const handlePromote = (reserveSpeler:any) =>{

        TeamService.promoteSpeler(reserveSpeler.id).then(() => {
            window.location.reload();
        }).catch(error=>{
            if (error.response.status===404) {
                navigate('/login');
            }
        });

    }

    
   
        return (
            <div>
               <h1>{dataObject.naam}</h1>
               <h2> - Manager: {dataObjectManager.naam}</h2>
               <p>
                   {role === "2" && isMijnTeam &&

                       <NavLink to={`/speler/registratie/${params.id}`}>Speler toevoegen </NavLink>
                   }
                     | <NavLink to={`/wedstrijden/team/${params.id}`}>Match historiek</NavLink> | <NavLink to={`/team/statistiek/${params.id}`}>Team Statiek</NavLink>
               </p>

               <h3>Spelers</h3>
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
                        {actieveSpelers.map((actieveSpeler:any)=>{
                            return(

                                <tr key={actieveSpeler.id}>
                                    <td>
                                        {actieveSpeler.speler.voornaam}
                                    </td>
                                    <td>
                                        {actieveSpeler.speler.naam}
                                    </td>
                                    <td>
                                        {actieveSpeler.speler.email}
                                    </td>
                                    <td>
                                        {actieveSpeler.speler.geboortedatum}
                                    </td>
                                    <td>
                                        <pre>{actieveSpeler.speler.adres.straat} {actieveSpeler.speler.adres.huisnr}</pre>
                                        <pre>{actieveSpeler.speler.adres.postcode} {actieveSpeler.speler.adres.gemeente}</pre>
                                    </td>
                                    <td>
                                        {role === "2" && isMijnTeam &&
                                            <>
                                                <button style={{marginRight: "1em"}} type="button"
                                                        className="btn btn-outline-danger"
                                                        onClick={() => handleDemote(actieveSpeler)}>
                                                    Demote
                                                </button>
                                                <NavLink to={`/teams/delete/${actieveSpeler.id}`}>
                                                    <button type="button" className="btn btn-outline-danger">
                                                        Verwijder
                                                    </button>
                                                </NavLink></>
                                        }

                                    </td>
                                </tr>
                            )})}
                    </tbody>

                </table>

                <h3>Reserve spelers</h3>
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
                    {reserveSpelers.map((reserveSpeler:any)=>{
                            return(

                                <tr key={reserveSpeler.id}>
                                    <td>
                                        {reserveSpeler.speler.voornaam}
                                    </td>
                                    <td>
                                        {reserveSpeler.speler.naam}
                                    </td>
                                    <td>
                                        {reserveSpeler.speler.email}
                                    </td>
                                    <td>
                                        {reserveSpeler.speler.geboortedatum}
                                    </td>
                                    <td>
                                        <pre>{reserveSpeler.speler.adres.straat} {reserveSpeler.speler.adres.huisnr}</pre>
                                        <pre>{reserveSpeler.speler.adres.postcode} {reserveSpeler.speler.adres.gemeente}</pre>
                                    </td>
                                    <td>
                                        {role === "2" && isMijnTeam &&
                                            <>
                                                <button style={{marginRight: "1em"}} type="button"
                                                        className="btn btn-outline-success"
                                                        onClick={() => handlePromote(reserveSpeler)}>
                                                    Promote
                                                </button>
                                                <NavLink to={`/teams/delete/${reserveSpeler.id}`}>
                                                    <button type="button" className="btn btn-outline-danger">
                                                        Verwijder
                                                    </button>
                                                </NavLink></>
                                        }

                                    </td>
                                </tr>
                            )})}
                    </tbody>

                </table>
                <NavLink to='/teams/manager'><button type="button" className="btn btn-outline-danger">Terug</button></NavLink>
                
            </div>
        );
    
}

export default TeamReadComponent;