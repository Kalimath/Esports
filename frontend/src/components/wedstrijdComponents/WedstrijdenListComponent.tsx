import { useEffect, useState } from "react";
import {NavLink, useNavigate} from "react-router-dom";
import WedstrijdService from "../../services/WedstrijdService";



export default function WedstrijdenListComponent() {
    const navigate = useNavigate();
    const [Wedstrijden, setWedstrijden] = useState([]);

    useEffect(() => {
        let abortController = new AbortController();
        async function fetchWedstrijden(){
            await WedstrijdService.getWedstrijden().then((res) => {
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
    },[navigate]);



    return (
        <div>
            <h1 className="titel">Wedstrijden</h1>
            <p>
                <NavLink to="/wedstrijden/create">Create Wedstrijd</NavLink>
            </p>
            <table className="table table-striped" >
                <thead className="table-dark">
                <tr>
                    <th>Team A</th>
                    <th >Score</th>
                    <th >Team B</th>
                    <th >Score</th>
                    <th>Datum</th>
                    <th>Tijdstip</th>
                </tr>
                </thead>
                <tbody>
                {
                    Wedstrijden.map((wedstrijd:any) =>
                        <tr key={wedstrijd.wedstrijdId}>
                            <td>{wedstrijd.teamA.naam} </td>
                            <td >{wedstrijd.scoreteama}</td>
                            <td >{wedstrijd.teamB.naam}</td>
                            <td >{wedstrijd.scoreteamb}</td>
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


