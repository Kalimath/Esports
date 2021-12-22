import React, {useEffect, useState} from 'react';
import {NavLink, useNavigate} from "react-router-dom";
import TeamService from "../../services/TeamService";

function TeamsVanManagerComponent () {
    const [Teams, setTeams] = useState([]);
    const role = localStorage.getItem("role");
    const navigate = useNavigate();
    useEffect(()=>{
        let abortController = new AbortController();
        async function fetchTeams(){
            await TeamService.getTeamsByManager(0).then((res) =>{
                setTeams(res.data);
            }).catch(error=>{
                if(error.response.status){
                    navigate('/teams');
                }
                if (error.response.status===404) {
                    navigate('/login');
                }
            });
        }
        fetchTeams();
        return () => {
            abortController.abort();
        }
    },[navigate]);
        return (
            <div>
                <h1 className="titel">Teams</h1>
                {role === "2" &&
                    <p>
                        <NavLink to='/teams/create'>Create team</NavLink>
                    </p>
                }


                <table className="table table-striped">
                    <thead className="table-dark">
                    <tr>
                        <th>Team naam</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        Teams.map((team:any)=>
                            <tr key = {team.id}>
                                <td>
                                    <NavLink to= {`/teams/team/${team.id}`}>
                                        {team.naam}
                                    </NavLink>
                                </td>
                                <td>
                                    {role === "2" &&
                                            <NavLink to={ `/teams/update/${team.id}` } >
                                                <button className="btn btn-outline-secondary btn-sm" >Update</button>
                                            </NavLink>
                                    }
                                </td>
                            </tr>
                        )
                    }
                    </tbody>

                </table>

            </div>
        );

}

export default TeamsVanManagerComponent;