import React, { useEffect, useState }  from 'react';
import { NavLink, useNavigate, useParams } from 'react-router-dom';
import TeamService from '../../services/TeamService';
import { useForm } from 'react-hook-form';


function TeamUpdateComponent() {
    const params = useParams();
    const navigate = useNavigate();
    const [team, setTeam] = useState({id: null,naam: '' });
    const {register, handleSubmit,formState: {errors},trigger,reset} = useForm();

    useEffect( ()=>{

        TeamService.getTeam(params.id).then( (res) =>{
            console.log(res.data);
            setTeam( res.data);
            reset(res.data);
         }).catch(error=>{
             console.log(error.response);
             if (error.response.status === 403){
                 navigate('/teams/manager');
             }
             if (error.response.status === 500){
                 navigate('/teams');
             }
            if (error.response.status===404) {
                navigate('/login');
            }
        });
    },[reset,params,navigate]);


    const onSubmit = (data: any)=>{

        TeamService.updateTeam(data,team.id).then(() =>{
            navigate('/teams/manager');
        }).catch(error=>{
            if (error.response.status===404) {
                navigate('/login');
            }
        });
    }
    return (
        <div>
            <h1 className="titel">Update team</h1>
            <form className="row g-3 "  onSubmit={handleSubmit(onSubmit)}>
                <div className="col-md-4">
                    <label  className="form-label">Team naam</label>
                    <input type="text" defaultValue={team.naam}  placeholder="Team naam" className={`form-control ${errors.naam && "invalid"}`}
                    {...register("naam",{required: "Team naam is verplicht"})}
                    onKeyUp={() => {
                        trigger("naam");
                      }}
                    />
                    {errors.naam && (<small className="text-danger">{(errors.naam as any).message}</small>)}
                </div>

                <div className="col-12">
                    <button style={{marginRight: "1em" }} type="submit" className="btn btn-outline-success">Update</button>
                    <NavLink to='/teams/manager'><button type="button" className="btn btn-outline-danger">Cancel</button></NavLink>
                </div>
            </form>
        </div>
    );
}

export default TeamUpdateComponent;