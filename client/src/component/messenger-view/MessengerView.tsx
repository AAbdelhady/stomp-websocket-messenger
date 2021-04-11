import React, {useEffect, useState} from 'react';
import classes from './MessengerView.module.scss';
import ConversationList from '../conversation-list/ConversationList';
import {fetchAuthorizedUserConversations} from '../../api/api';
import {Conversation} from '../../model/conversation';
import ConversationView from '../conversation/ConversationView';

const MessengerView = () => {
    const [conversations, setConversations] = useState<Conversation[]>([])

    const [currentConversation, setCurrentConversation] = useState<Conversation | null>(null)

    useEffect(() => {
        fetchAuthorizedUserConversations().then(res => { setConversations(res.data) })
    }, [])

    return (
        <div className={classes.Container}>
            <div className={classes.ConversationListContainer}>
                <ConversationList conversations={conversations} setCurrentConversation={setCurrentConversation}/>
            </div>
            <div className={classes.ConversationViewContainer}>
                <ConversationView conversation={currentConversation}/>
            </div>
        </div>
    );
}

export default MessengerView;
