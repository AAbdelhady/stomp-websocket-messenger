import React from 'react';
import classes from './ConversationList.module.scss';
import {Conversation} from '../../model/conversation';
import ConversationTile from './ConversationTile';
import CreateConversation from './CreateConversation';

interface Props {
    conversations: Conversation[],
    setCurrentConversation: (c: Conversation) => void
}

const ConversationList = ({conversations, setCurrentConversation}: Props) => {
    const tiles = conversations.length > 0 ?
        conversations.map(c => <ConversationTile conversation={c} conversationTileClicked={() => setCurrentConversation(c)} key={c.id}/>) :
        <p className={classes.NoConversationsText}>No conversations</p>
    return (
        <div className={classes.Container}>
            <div className={classes.ConversationTilesContainer}>{tiles}</div>
            <div className={classes.CreateConversationContainer}><CreateConversation/></div>
        </div>
    );
}

export default ConversationList;
