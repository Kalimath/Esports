import React from 'react';
import './App.css';
import { BrowserRouter as Router, Route , Routes } from 'react-router-dom';
import ListSpelersComponent from './components/spelerCoponents/ListSpelersComponent';
import HeaderComponent from './components/HeaderComponent';
import HomeComponent from './components/HomeComponent';
import SpelerCreateComponent from './components/spelerCoponents/SpelerCreateComponent';
import SpelerUpdateComponent from './components/spelerCoponents/SpelerUpdateComponent';
import TeamListComponent from './components/teamComponents/TeamListComponent';
import TeamCreateComponent from './components/teamComponents/TeamCreateComponent';
import TeamUpdateComponent from './components/teamComponents/TeamUpdateComponent';
import TeamReadComponent from './components/teamComponents/TeamReadComponent';
import TeamVoegSpelerToeComponent from './components/teamComponents/TeamVoegSpelerToeComponent';
import TeamDeleteSpeler from "./components/teamComponents/TeamDeleteSpeler";
import WedstrijdenListComponent from "./components/wedstrijdComponents/WedstrijdenListComponent";
import WedstrijdCreateComponent from "./components/wedstrijdComponents/WedstrijdCreateComponent";
import WedstrijdUpdateComponent from "./components/wedstrijdComponents/WedstrijdUpdateComponent";
import WedstrijdenHistoriekTeamComponent from "./components/wedstrijdComponents/WedstrijdHistoriekTeam";
import WedstrijdHistoriekSpeler from "./components/wedstrijdComponents/WedstrijdHistoriekSpeler";
import ListManagersComponent from './components/managerComponents/ListManagersComponent';
import ManagerCreateComponent from './components/managerComponents/ManagerCreateComponent';
import UpdateManagerComponent from './components/managerComponents/UpdateManagerComponent';
import TeamStatistiekenComponent from "./components/teamComponents/TeamStatistiekenComponent";
import LoginComponent from "./components/Login/LoginComponent";
import SpelerRead from "./components/spelerCoponents/SpelerRead";
import TeamsVanManagerComponent from "./components/teamComponents/TeamsVanManagerComponent";
import ManagerReadComponenet from "./components/managerComponents/ManagerReadComponenet";

function App() {
  return (
    <div>
      <Router>
          <HeaderComponent/>
             <div className="container">
               <Routes>
                    <Route path='/' caseSensitive={false} element={<HomeComponent/>} />
                    <Route  path='/spelers' caseSensitive={false} element={<ListSpelersComponent/>}/>
                    <Route path='/spelers/create' caseSensitive={false} element={<SpelerCreateComponent/>}/>
                    <Route path='/spelers/update/:id' caseSensitive={false} element={<SpelerUpdateComponent />}/>
                    <Route  path='/teams' caseSensitive={false} element={<TeamListComponent/>}/>
                    <Route  path='/teams/create' caseSensitive={false} element={<TeamCreateComponent/>}/>
                    <Route  path='/teams/update/:id' caseSensitive={false} element={<TeamUpdateComponent/>}/>
                    <Route path='/teams/team/:id' caseSensitive={false} element={<TeamReadComponent/>}/>
                    <Route path='/speler/registratie/:id' caseSensitive={false} element={<TeamVoegSpelerToeComponent/>}/>
                    <Route path='/teams/delete/:id' caseSensitive={false} element={<TeamDeleteSpeler/>}/>
                    <Route path='/team/statistiek/:id' caseSensitive={false} element={<TeamStatistiekenComponent/>}/>
                    <Route path='/wedstrijden' caseSensitive={false} element={<WedstrijdenListComponent/>}/>
                    <Route path='/wedstrijden/create' caseSensitive={false} element={<WedstrijdCreateComponent/>}/>
                    <Route path='/wedstrijden/update/:id' caseSensitive={false} element={<WedstrijdUpdateComponent/>}/>
                    <Route path='/wedstrijden/team/:id' caseSensitive={false} element={<WedstrijdenHistoriekTeamComponent/>}/>
                    <Route path='/wedstrijden/speler/:id' caseSensitive={false} element={<WedstrijdHistoriekSpeler/>}/>
                    <Route path= '/managers' caseSensitive={false} element={<ListManagersComponent/>}/>
                    <Route path= '/managers/create' caseSensitive={false} element={<ManagerCreateComponent/>}/>
                    <Route path= '/managers/update/:id' caseSensitive={false} element={<UpdateManagerComponent/>}/>
                    <Route path= '/login' caseSensitive={false} element={<LoginComponent/>}/>
                    <Route path= '/spelers/read' caseSensitive={false} element={<SpelerRead/>}/>
                    <Route path= '/teams/manager' caseSensitive={false} element={<TeamsVanManagerComponent/>}/>
                    <Route path= '/managers/read/:id' caseSensitive={false} element={<ManagerReadComponenet/>}/>
               </Routes>
             </div>
          
      </Router>
    </div>
    
  );
}
export default App;
