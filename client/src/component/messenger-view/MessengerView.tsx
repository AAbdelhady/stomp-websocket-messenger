import React, {useEffect, useState} from 'react';
import classes from './MessengerView.module.scss';
import ConversationList from '../conversation-list/ConversationList';
import {fetchAuthorizedUserConversations, logout} from '../../api/api';
import {Conversation} from '../../model/conversation';
import ConversationView from '../conversation/ConversationView';

const MessengerView = () => {
    const [conversations, setConversations] = useState<Conversation[]>([])
    const [currentConversation, setCurrentConversation] = useState<Conversation | null>(null)

    useEffect(() => {
        fetchAuthorizedUserConversations().then(res => {
            const fetched = res.data
            setConversations(fetched)
            if (fetched.length > 0) {
                setCurrentConversation(fetched[0])
            }
        })
    }, [])

    return (
        <div className={classes.Container}>
            <div className={classes.Header}>
                <button onClick={() => logout()} style={{float: 'right'}}>Logout</button>
            </div>
            <div className={classes.Body}>
                <div className={classes.ConversationListContainer}>
                    <ConversationList conversations={conversations} setCurrentConversation={setCurrentConversation}/>
                </div>
                <div className={classes.ConversationViewContainer}>
                    {currentConversation && <ConversationView conversation={currentConversation}/>}
                </div>
            </div>
        </div>
    );
}

export default MessengerView;
