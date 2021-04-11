import React from 'react';
import './App.scss';
import Login from './component/login/Login';
import useAuthorizedUser from './hooks/useAuthorizedUser';
import MessengerView from './component/messenger-view/MessengerView';

function App() {
    const authorizedUser = useAuthorizedUser()
    // const mainContent = authorizedUser ? <img src={logo} className="App-logo" alt="logo"/> : <Login/>
    const mainContent = authorizedUser ? <MessengerView/> : <Login/>
    return (
        <div className="App">
            <header className="App-header">
                {mainContent}
            </header>
        </div>
    );
}

export default App;
