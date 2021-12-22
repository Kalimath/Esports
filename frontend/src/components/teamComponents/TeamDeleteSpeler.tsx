import React, {useEffect, useState} from 'react';
import TeamService from "../../services/TeamService";
import {NavLink, useNavigate, useParams} from "react-router-dom";


function TeamDeleteSpeler (){
    const params = useParams();
    const navigate = useNavigate();
    const [spelerTeam,setSpelerTeam] = useState({id:null,isreserve:false, speler:{voornaam:'',naam:''},team:{id:null,naam:''}});

    useEffect(()=>{
        TeamService.getSpelerTeam(params.id).then((res)=> {
            console.log(res.data);
            setSpelerTeam(res.data);
        }).catch(error=>{navigate('/login')});

    },[params,navigate]);

    const handleDelete = () => {

        TeamService.deleteSpelerFromTeam(params.id).then((res) =>{
            navigate(`/teams/team/${spelerTeam.team.id}`);
        }).catch(error=>{
            if (error.response.status===404) {
                navigate('/login');
            }
        });
    }

        return (
            <div>
                <h1>{spelerTeam.speler.voornaam} {spelerTeam.speler.naam} verwijderen</h1>
                <p style={{marginTop: "3em" }} className="alert alert-warning" role="alert">
                    U staat op het punt deze speler te verwijderen uit {spelerTeam.team.naam}.
                </p>
                <p className="alert alert-warning" role="alert"> Bent u zeker dat u dit wilt doen.</p>
                <button style={{marginRight: "1em" }} type="button" className="btn btn-outline-success" onClick={handleDelete}>Ok</button>
                <NavLink to={`/teams/team/${spelerTeam.team.id}`}>
                    <button type="button" className="btn btn-outline-danger">Annuleer</button>
                </NavLink>

            </div>
        );

}

export default TeamDeleteSpeler;