import React, {useEffect, useState} from "react";
import {NavLink, useNavigate} from "react-router-dom";
import {useForm} from "react-hook-form";
import WedstrijdService from "../../services/WedstrijdService";
import TeamService from "../../services/TeamService";


function ValidateDate(date: string): boolean{

    let momenteel = new Date();
    //Today
    let todayDay = momenteel.getDate();
    let todayMonth = momenteel.getMonth() + 1;
    let todayYear = momenteel.getFullYear();

    //User input
    let userDateDay = Number.parseInt(date.split("T")[0].split("-")[2]);
    let userDateMonth = Number.parseInt(date.split("T")[0].split("-")[1]) - 1;
    let userDateYear =  Number.parseInt(date.split("T")[0].split("-")[0]);

    let userDate = new Date(userDateYear, userDateMonth, userDateDay)

    console.log(todayDay, todayMonth, todayYear);
    console.log(userDateDay, userDateMonth, userDateYear);

    console.log("current day: " + momenteel);
    console.log("picker day: " + userDate);

    if(userDate < momenteel){
        return false
    } else {
        return true
    }
}


function WedstrijdCreateComponent(){
    const navigate = useNavigate();
    const [teamsManager,setTeamsManager]=useState([])
    const [Teams, setTeams] = useState([]);
    const [dataObject, setData] = useState(null);
    const [dataObject2, setData2] = useState(null);
    const [sameTeamError, setTeamError] = useState(""); 

    const {register, handleSubmit,formState: {errors},trigger} = useForm()

    useEffect(() => {
        let abortController = new AbortController();
        async function fetchTeams(){
            TeamService.getTeams().then((res) =>{
            setTeams(res.data);
            }).catch(error=>{
                if (error.response.status===404) {
                    navigate('/login');
                }
            });
            TeamService.getTeamsByManager(0).then((res) =>{
               setTeamsManager(res.data);
            }).catch(error=>{
                if (error.response.status ===404){
                    navigate('/login');
                }
            });
        }
        fetchTeams();
        return () => {
            abortController.abort();
        }

    }, [navigate])

    const handleChange =(e:any) =>{
        setData(e.target.value);
        setTeamError("");
    }

    const handleChange2 =(e:any) =>{
        setData2(e.target.value);
        setTeamError("");
    };

    const onSubmit = (data: any)=>{
        if(dataObject === dataObject2){
             setTeamError("Kies twee verschillende teams.");
             return
        }
        if(!ValidateDate(data.tijdstip)){
            setTeamError("Enkel wedstrijden in de toekomst mogelijk.")
            return
        }
        let wedstrijdDTO = { team_id_1: dataObject, team_id_2: dataObject2, tijdstip: data.tijdstip }
        WedstrijdService.createWedstrijd(wedstrijdDTO).then(res =>{
            navigate('/wedstrijden');
        }).catch(error=>{
            if (error.response.status===404) {
                navigate('/login');
            }
        });
    }

    return (
        <div>
            <h1 className="titel">Create Wedstrijd</h1>
            <form className="row g-3 "  onSubmit={handleSubmit(onSubmit)}>
                <div className="col-md-4">
                    <label  className="form-label">Team 1</label>
                    <select className="form-select"  onChange={handleChange}>
                        <option value="0"></option>
                        {teamsManager.map((team:any) =>{
                            return(
                                <option key={team.id} value={team.id}>{team.naam}</option>
                            )})}
                    </select>
                </div>
                <div className="col-md-4">
                    <label  className="form-label">Team 2</label>
                    <select className="form-select"  onChange={handleChange2} >
                        <option value="0"></option>
                        {Teams.map((team: any) => {
                            return (
                                <option key={team.id} value={team.id}>{team.naam}</option>
                            )
                        })}
                    </select>
                </div>
                <div className="col-md-4">
                    <label  className="form-label">datum en tijdstip</label>
                    <input type="datetime-local"  placeholder="wedstrijd tijdstip" className={`form-control ${errors.tijdstip && "invalid"}`}
                             {...register("tijdstip",{required: "tijdstip is verplicht"})}
                             onKeyUp={() => {
                                 trigger("tijdstip");
                             }}
                    />
                    {errors.tijdstip && (<small className="text-danger">{errors.tijdstip.message}</small>)}
                </div>

                <div className="col-12">
                    <button style={{marginRight: "1em" }} type="submit" className="btn btn-outline-success">Create</button>
                    <NavLink to='/wedstrijden'><button type="button" className="btn btn-outline-danger">Cancel</button></NavLink>
                </div>
                <p className="text-danger">{sameTeamError}</p>
            </form>
        </div>
    );
}


export default WedstrijdCreateComponent;