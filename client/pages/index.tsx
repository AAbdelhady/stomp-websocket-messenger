import React from 'react';
import classes from './App.module.scss';
import useAuthorizedUser from '../hooks/useAuthorizedUser';
import useStompClient from '../hooks/useStompClient';
import MessengerView from '../component/messenger-view/MessengerView';
import Login from '../component/login/Login';
import StompClientContext from '../contexts/stompClientContext';
import AuthorizedUserContext from '../contexts/authorizedUserContext';
import Head from 'next/head';

const App = () => {
    const authorizedUser = useAuthorizedUser()
    const stompClient = useStompClient(authorizedUser)
    const mainContent = authorizedUser ? <MessengerView/> : <Login/>
    return (
        <>
            <Head>
                <title>WebSocket Messenger</title>
            </Head>
            <AuthorizedUserContext.Provider value={authorizedUser}>
                <StompClientContext.Provider value={stompClient}>
                    <div className={classes.App}>
                        <header className={classes.AppHeader}>
                            {mainContent}
                        </header>
                    </div>
                </StompClientContext.Provider>
            </AuthorizedUserContext.Provider>
        </>
    )
}

export default App
