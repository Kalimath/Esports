import axios from "axios";

const SPELER_API_BASE_URL = "/api/spelers";

class SpelerService {
    getSpelers(){
        return axios.get(SPELER_API_BASE_URL);
    }
    getSpelersNietInTeam(id:any){
        return axios.get(SPELER_API_BASE_URL+'/team/'+id);
    }
    getSpeler(id:any){
        return axios.get(SPELER_API_BASE_URL+'/'+id);
    }

    getActieveSpelersVanEenTeam(id:any){
        return axios.get(SPELER_API_BASE_URL+'/actief/'+id);

    }
    getReserveSpelersVanEenTeam(id:any){
        return axios.get(SPELER_API_BASE_URL+'/reserve/'+id);
    }
    createSpeler(spelerDTO: any){
        return axios.post(SPELER_API_BASE_URL, spelerDTO);
    }
    updateSpeler(spelerDTO: any,id: any){
        return axios.put(SPELER_API_BASE_URL+'/'+ id,spelerDTO);
    }
}
export default new SpelerService()