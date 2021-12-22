import React  from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import TeamService from '../../services/TeamService';
import { useForm } from 'react-hook-form';

function TeamCreateComponent(){
    const navigate = useNavigate();

   
    const {register, handleSubmit,formState: {errors},trigger} = useForm()
   

    const onSubmit = (data: any)=>{
        
            TeamService.createTeam(data).then(res =>{
              
                navigate('/teams/manager');
            }).catch(error=>{
                if (error.response.status===404) {
                    navigate('/login');
                }
            });
    }

    
    
    return (
        <div>
            <h1 className="titel">Create team</h1>
            <form className="row g-3 "  onSubmit={handleSubmit(onSubmit)}>
                <div className="col-md-4">
                    <label  className="form-label">Team naam</label>
                    <input   type="text"  placeholder="Naam"  className={`form-control ${errors.naam && "invalid"}`} 
                    {...register("naam",{required: "Team naam is verplicht"})}
                    onKeyUp={() => {
                        trigger("naam");
                      }}
                    />
                    {errors.naam && (<small className="text-danger">{errors.naam.message}</small>)}
                </div>
                
                <div className="col-12">
                    <button style={{marginRight: "1em" }} type="submit" className="btn btn-outline-success">Create</button>
                    <NavLink to='/teams/manager'><button type="button" className="btn btn-outline-danger">Cancel</button></NavLink>
                </div>
            </form>
        </div>
    );
}

export default TeamCreateComponent;