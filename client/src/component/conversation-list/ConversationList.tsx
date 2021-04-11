import React, {Dispatch} from 'react';
import classes from './ConversationList.module.scss';
import {Conversation} from '../../model/conversation';
import ConversationTile from './ConversationTile';

interface Props {
    conversations: Conversation[],
    setCurrentConversation: Dispatch<Conversation>
}

const ConversationList = ({conversations, setCurrentConversation}: Props) => {
    const tiles = conversations.map(c => <ConversationTile conversation={c} conversationTileClicked={() => setCurrentConversation(c)} key={c.id}/>)
    return (
        <div className={classes.Container}>
            {tiles}
        </div>
    );
}

export default ConversationList;
