import axios from "axios";

const TEAM_API_BASE_URL = "/api/teams";
const REGISTRATIE_API_BASE_URL ="/api/registraties";
const TEAM_STATISTIEK_API_BASE_URL ="/api/teams/statistiek";

class TeamService {
    getTeams(){
        return axios.get(TEAM_API_BASE_URL);
    }
    getTeamsByManager(id:any){
        return axios.get(TEAM_API_BASE_URL+'/manager/'+id);
    }
    getTeam(id: any){
        return axios.get(TEAM_API_BASE_URL+'/'+id)
    }
    getSpelerTeam(id:any){
        return axios.get(REGISTRATIE_API_BASE_URL+'/kick/'+id);
    }

    createTeam(team: any){
        return axios.post(TEAM_API_BASE_URL, team);
    }
    updateTeam(teamDTO: any,id: any){
        return axios.put(TEAM_API_BASE_URL+'/'+ id,teamDTO);
    }
    voegSpelerToeAanTeam(registratieDTO:any){
        return axios.post(REGISTRATIE_API_BASE_URL+'/reserve',registratieDTO );
    }
    demoteSpeler(id:any){
        return axios.put(REGISTRATIE_API_BASE_URL+'/demote/'+ id);
    }
    promoteSpeler(id:any){
        return axios.put(REGISTRATIE_API_BASE_URL+'/promote/'+ id);
    }
    deleteSpelerFromTeam(id:any){
        return axios.delete(REGISTRATIE_API_BASE_URL+ '/delete/'+id);
    }
    teamStatiekFromTeam(id:any){
        return axios.get(TEAM_STATISTIEK_API_BASE_URL +"/" +id);
    }
    teamBehoortTotManager(id:any){
        return axios.get(TEAM_API_BASE_URL+'/ismijn/team/'+id);
    }


}
export default new TeamService()