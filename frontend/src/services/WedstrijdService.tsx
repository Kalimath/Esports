import axios from "axios" 

const WEDSTRIJD_API_BASE_URL = "/api/wedstrijden/dto"; 
const WEDSTRIJD_API_GET_BY_ID = "/api/wedstrijden/scoreDTO/";
const WEDSTRIJD_UPDATE_SCORE = "/api/wedstrijden/";

class WedstrijdService {
    getWedstrijden(){
        return axios.get(WEDSTRIJD_API_BASE_URL);
    }
    createWedstrijd(wedstrijdDTO: any){
        return axios.post(WEDSTRIJD_UPDATE_SCORE, wedstrijdDTO);
    }
    getScoreDTO(id: any){
        return axios.get(WEDSTRIJD_API_GET_BY_ID + id)
    }
    updateScore(id: any, scoreDTO: any){
        return axios.put(WEDSTRIJD_UPDATE_SCORE + id, scoreDTO);
    }
    getWedstrijdenTeam(id:any){
        return axios.get(WEDSTRIJD_UPDATE_SCORE+'/team/'+id);
    }
    getWedstrijdenSpeler(id:any){
        return axios.get(WEDSTRIJD_UPDATE_SCORE+ '/speler/' + id);
    }

}

export default new WedstrijdService();




