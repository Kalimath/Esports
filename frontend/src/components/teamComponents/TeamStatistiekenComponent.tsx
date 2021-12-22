import React, { useEffect, useState }  from 'react';
import { NavLink, useNavigate, useParams } from 'react-router-dom';
import TeamService from '../../services/TeamService';



function TeamStatistiekenComponent(){
    const params = useParams();
    const navigate = useNavigate();
    const [statistiek, setStatistiek] = useState({wins: null, losses: null, wn8: null})



    useEffect(()=>{
        TeamService.teamStatiekFromTeam(params.id).then((res)=>{
            setStatistiek(res.data);
        }).catch(error=>{
            if (error.response.status===404) {
                navigate('/login');
            }
        });

    },[params]);


    return(
      <div>
          <h1 className="titel">Statistieken</h1>
          <table className="table table-striped">
              <thead className="table-dark">
          <tr>
              <th>Wins</th>
              <th>Losses</th>
              <th>Win percentage</th>
          </tr>
              </thead>
              <tbody>
                <tr>
                    <td>{statistiek.wins}</td>
                    <td>{statistiek.losses}</td>
                    <td>{statistiek.wn8} %</td>
                </tr>
              </tbody>
          </table>
          <NavLink to={`/teams/team/${params.id}`}><button type="button" className="btn btn-outline-danger">Terug</button></NavLink>
      </div>
    );

}
export default TeamStatistiekenComponent;