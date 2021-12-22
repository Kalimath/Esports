import React, { useEffect, useState } from "react";
import {NavLink, useNavigate, useParams} from "react-router-dom";
import WedstrijdService from "../../services/WedstrijdService";



export default function WedstrijdHistoriekSpeler() {
    const params = useParams();
    const navigate = useNavigate();
    const [Wedstrijden, setWedstrijden] = useState([]);
    const role = localStorage.getItem('role');

    useEffect(() => {
        let abortController = new AbortController();
         function fetchWedstrijden(){
             WedstrijdService.getWedstrijdenSpeler(params.id).then((res) => {

                setWedstrijden(res.data);
            }).catch(error=>{
                console.log(error.response)
                if (error.response.status === 403 ){

                }
                if(error.response.status === 404){
                    navigate('/login')
                }
            });
        }
        fetchWedstrijden();
        return() => {
            abortController.abort();
        }
    },[params]);

    return (
        <div>
            {role ==="2" &&
                <h1 className="text-danger">Geen toegang u bent een manager</h1>
            }
            {role === "1" &&
                <><h1 className="titel">Wedstrijden</h1>
                    <table className="table table-striped">
                        <thead className="table-dark">
                        <tr>
                            <th>Team A</th>
                            <th className="wedstrijdscore">Score</th>
                            <th className="wedstrijdscore">Team B</th>
                            <th className="wedstrijdscore">Score</th>
                            <th>Datum</th>
                            <th>Tijdstip</th>
                        </tr>
                        </thead>
                        <tbody>
                        {Wedstrijden.map((wedstrijd: any) => <tr key={wedstrijd.wedstrijdId}>
                                <td>{wedstrijd.teamA.naam} </td>
                                <td className="wedstrijdscore">{wedstrijd.scoreteama}</td>
                                <td className="wedstrijdscore">{wedstrijd.teamB.naam}</td>
                                <td className="wedstrijdscore">{wedstrijd.scoreteamb}</td>
                                <td>{wedstrijd.tijdstip.split("T")[0]}</td>
                                <td>{wedstrijd.tijdstip.split("T")[1].split(":")[0]}:{wedstrijd.tijdstip.split("T")[1].split(":")[1]}</td>
                            </tr>
                        )}
                        </tbody>
                    </table>
                </>
            }


        </div>
    )
}