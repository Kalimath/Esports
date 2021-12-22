import React, { useEffect, useState } from "react";
import {NavLink, useNavigate, useParams} from "react-router-dom";
import WedstrijdService from "../../services/WedstrijdService";



export default function WedstrijdenHistoriekTeamComponent() {
    const params = useParams();
    const navigate = useNavigate();
    const [Wedstrijden, setWedstrijden] = useState([]);
    const role = localStorage.getItem("role");

    useEffect(() => {
        let abortController = new AbortController();
        async function fetchWedstrijden(){
            await WedstrijdService.getWedstrijdenTeam(params.id).then((res) => {
                setWedstrijden(res.data);
            }).catch(error=>{
                if (error.response.status===404) {
                    navigate('/login');
                }
            });
        }
        fetchWedstrijden();
        return() => {
            abortController.abort();
        }
    },[params,navigate]);

    return (
        <div>
            <h1 className="titel">Wedstrijden</h1>
            <div style={{marginTop: "1em" ,marginBottom:"1em"}}><NavLink to={`/teams/team/${params.id}`}><button type="button" className="btn btn-outline-danger">Terug</button></NavLink></div>
            <table className="table table-striped" >
                <thead className="table-dark">
                <tr>
                    <th></th>
                    <th>Team A</th>
                    <th className="wedstrijdscore">Score</th>
                    <th className="wedstrijdscore">Team B</th>
                    <th className="wedstrijdscore">Score</th>
                    <th>Datum</th>
                    <th>Tijdstip</th>
                </tr>
                </thead>
                <tbody>
                {Wedstrijden.map((wedstrijd:any) =>
                        <tr key={wedstrijd.wedstrijdId}>
                            {role === "2" &&
                                <td><NavLink to={`/wedstrijden/update/${wedstrijd.wedstrijdId}`}>Update</NavLink></td>
                            }
                            <td>{wedstrijd.teamA.naam} </td>
                            <td className="wedstrijdscore">{wedstrijd.scoreteama}</td>
                            <td className="wedstrijdscore">{wedstrijd.teamB.naam}</td>
                            <td className="wedstrijdscore">{wedstrijd.scoreteamb}</td>
                            <td>{wedstrijd.tijdstip.split("T")[0]}</td>
                            <td>{wedstrijd.tijdstip.split("T")[1].split(":")[0]}:{wedstrijd.tijdstip.split("T")[1].split(":")[1]}</td>
                        </tr>
                    )
                }
                </tbody>
            </table>

        </div>
    )
}