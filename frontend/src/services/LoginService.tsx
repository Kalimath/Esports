import axios from "axios";


const REGISTRATIE_BASE_URL = "/api/registraties/auth";

class LoginService{
    login(data:any){
        return axios({
            method:'post',
            url:'/api/perform_login',
            params: { username:data.username,password:data.password},
            headers: {'Content-Type': 'application/json'}
        }).catch(error=>{
            console.log(error);
        });
    }
    logout(){
        return axios({
            method:'post',
            url:'/api/perform_logout'
        });
    }
    getRoleIngelogdeGebruiker(){
        return axios.get(REGISTRATIE_BASE_URL);
    }

}
export default new LoginService();