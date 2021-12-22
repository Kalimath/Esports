import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { NavLink, useNavigate, useParams } from "react-router-dom"
import WedstrijdService from "../../services/WedstrijdService";

function WedstrijdUpdateScoreComponent() {
    const WedstrijdDTO = {
        teamA:{
            id: null,
            naam:''
        },
        scoreteama: 0,
        teamB:{
            id: null,
            naam: ''
        },
        scoreteamb: 0,
        wedstrijdId: null
    };



    const params = useParams();
    const [Wedstrijd, setWedstrijden] = useState(WedstrijdDTO);
    const {register, handleSubmit, reset} = useForm();
    const navigate = useNavigate();

    useEffect( () => {
        WedstrijdService.getScoreDTO(params.id).then((res) => {
            setWedstrijden(res.data);
            reset(res.data);
        }).catch(error =>{ navigate('/login')});
    }, [reset,params,navigate]);

    const onSubmit = (data: any) => {
        console.log(data);
        let scoreDTO ={scoreteama: data.scoreteama, scoreteamb: data.scoreteamb, teamA: data.teamA.id, teamB: data.teamB.id};
        console.log(scoreDTO);
        WedstrijdService.updateScore(params.id, scoreDTO).then( () =>{
            navigate('/wedstrijden');
        }).catch(error=>{
            if (error.response.status===404) {
                navigate('/login');
            }
        });

    }
    return (
        <div>
            <h1 style={{marginBottom: "1em"}}>Update Score Wedstrijd:  {params.id}</h1>
            <form className="row g-3" onSubmit={handleSubmit(onSubmit)}>
                <div className="col-md-3">
                    <h2 className="form-label">{Wedstrijd.teamA.naam}</h2>
                    <input style={{width:"4em"}} type="number" defaultValue={Wedstrijd.scoreteama} min={0}
                           {...register("scoreteama", {valueAsNumber: true} )}/>
                </div>
                <div className="col-md-3">
                    <h2 className="form-label">{Wedstrijd.teamB.naam}</h2>
                    <input style={{width:"4em"}} type="number" min={0} defaultValue={Wedstrijd.scoreteamb}
                           {...register("scoreteamb", {valueAsNumber: true} )}/>
                </div>

                <div className="col-12">
                    <button style={{marginRight: "1em" }} type="submit" className="btn btn-outline-success">Update</button>
                    <NavLink to='/wedstrijden'><button type="button" className="btn btn-outline-danger">Cancel</button></NavLink>
                </div>
            </form>
        </div>
    )
}

export default WedstrijdUpdateScoreComponent;