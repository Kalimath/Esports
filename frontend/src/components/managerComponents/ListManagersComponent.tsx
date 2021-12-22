import { useEffect, useState } from "react";
import ManagerService from "../../services/ManagersService";
import {useNavigate} from "react-router-dom";

function ListManagersComponent (){
    const [Managers, setManagers] = useState([]);
    const navigate = useNavigate();

    useEffect( () => {
            ManagerService.getManagers().then((res) => {
                console.log(res);
                setManagers(res.data);
            }).catch(error=>{
                if (error.response.status === 404){
                    navigate("/login")
                }
            });
        },[navigate]
    );

    console.log(Managers); 

    return (
        <div>
            <h1 className="titel">Managers</h1>
            <table className="table table-striped">
                <thead className="table-dark">
                    <tr>
                        <th>Naam</th>
                        <th>E-mail</th>
                    </tr>
                </thead>
                <tbody>
                    {
                        Managers.map((manager: any) =>
                            <tr key={manager.id}>
                                <td>
                                    {manager.naam}
                                </td>
                                <td>
                                    {manager.email}
                                </td>
                            </tr>
                        )
                    } 
                </tbody>
            </table>
        </div>
    );

}

export default ListManagersComponent; 

