import React, { useEffect, useState } from 'react';
import { NavLink, useNavigate, useParams } from 'react-router-dom';
import TeamService from '../../services/TeamService';
import { useForm } from 'react-hook-form';
import SpelerService from '../../services/SpelerService';

function TeamVoegSpelerToeComponent () {
    const navigate = useNavigate();
    const params = useParams();
    const [Spelers, setSpelers] = useState([]);
    const [dataObject,setData]= useState(null);
    const [checkeStatus,setCheckStatus] = useState(false);
    const [error,setError] = useState("");
   
    const { handleSubmit} = useForm()
   

     useEffect(()=>{
         let abortController = new AbortController();
         async function fetchSpelers(){
             SpelerService.getSpelersNietInTeam(params.id).then((res) =>{
                 setSpelers(res.data);
             }).catch(error=>{
                 if (error.response.status===404) {
                     navigate('/login');
                 }
             });
         }
         fetchSpelers();
         return () => {
             abortController.abort();
         }

     },[navigate,params]);

    const handleChange =(e:any) =>{
        setData(e.target.value);
     }
    const isChecked = (e:any) =>{
        const checked = e.target.checked;
        if(checked){
            setCheckStatus(true);
        }else{
            setCheckStatus(false);
        }
     };

    const onSubmit = ()=>{
         
         let registratieDTO = {team_id: params.id, speler_id: dataObject, isreserve: checkeStatus };
        
         TeamService.voegSpelerToeAanTeam(registratieDTO).then((res)=>{
             navigate(`/teams/team/${params.id}`);
         }).catch(error=>{
             if (error.response.status === 403){
                 setError(error.response.data);
             }
             if (error.response.status===404) {
                 navigate('/login');
             }
         });
    }
    
    return (
        <div>
            <h1 className="titel">Voeg speler toe</h1>
            <p className="text-danger">{error}</p>
            <form className="row g-3 "  onSubmit={handleSubmit(onSubmit)}>
                <div className="col-md-4">
                    <label  className="form-label">Speler</label>
                    <select className="form-select"  onChange={handleChange}>
                        <option value="0"></option>
                        {Spelers.map((speler:any) =>{
                            return(
                                <option key={speler.id} value={speler.id}>{speler.voornaam} {speler.naam}</option>
                            )})}
                    </select>
                </div>    
                <div className="col-md-4 form-check">
                    <label className ="form-check-label" htmlFor="exampleCheck1">reserve</label>
                    <input type="checkbox" className="form-check-input" id="exampleCheck1" onClick={(e) => {isChecked(e)}}/>

                </div>
                <div className="col-12">
                    <button style={{marginRight: "1em" }} type="submit" className="btn btn-outline-success">Voeg speler toe</button>
                    <NavLink to={`/teams/team/${params.id}`}><button type="button" className="btn btn-outline-danger">Annuleer</button></NavLink>
                </div>
            </form>
        </div>
    );  
}

export default TeamVoegSpelerToeComponent;