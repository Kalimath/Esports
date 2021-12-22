import axios from "axios"; 

const MANAGER_API_BASE_URL = "/api/managers"; 

class ManagerService{
    getManagers(){
        return axios.get(MANAGER_API_BASE_URL); 
    }
    getManagerById(id: any){
        return axios.get(MANAGER_API_BASE_URL + "/" + id);
    }
    createManager(ManagerDTO: any){
        return axios.post(MANAGER_API_BASE_URL, ManagerDTO);
    }
    updateManager(id: any, ManagerDTO: any){
        return axios.put(MANAGER_API_BASE_URL + "/" + id, ManagerDTO);
    }
}

export default new ManagerService();