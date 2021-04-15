import React from 'react';
import './App.scss';
import Login from './component/login/Login';
import useAuthorizedUser from './hooks/useAuthorizedUser';
import MessengerView from './component/messenger-view/MessengerView';
import AuthorizedUserContext from './contexts/authorizedUserContext';
import StompClientContext from './contexts/stompClientContext';
import useStompClient from './hooks/useStompClient';

function App() {
    const authorizedUser = useAuthorizedUser()
    const stompClient = useStompClient(authorizedUser)
    const mainContent = authorizedUser ? <MessengerView/> : <Login/>
    return (
        <AuthorizedUserContext.Provider value={authorizedUser}>
            <StompClientContext.Provider value={stompClient}>
                <div className="App">
                    <header className="App-header">
                        {mainContent}
                    </header>
                </div>
            </StompClientContext.Provider>
        </AuthorizedUserContext.Provider>
    );
}

export default App;
