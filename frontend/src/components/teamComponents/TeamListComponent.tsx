import React, { useEffect, useState} from 'react';
import TeamService from '../../services/TeamService';
import {NavLink, useNavigate} from 'react-router-dom';


function TeamListComponent () {
    const [Teams, setTeams] = useState([]);
    const navigate = useNavigate();
    useEffect(()=>{
        let abortController = new AbortController();
        async function fetchTeams(){
             await TeamService.getTeams().then((res) =>{
                setTeams(res.data);
            }).catch(error=>{
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


                <table className="table table-striped">
                    <thead className="table-dark">
                        <tr>
                            <th>Team naam</th>

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
                                </tr>
                            )
                        }
                    </tbody>

                </table>
                
            </div>
        );

}

export default TeamListComponent;