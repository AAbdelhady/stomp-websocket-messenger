import React, {useEffect, useState} from 'react';
import classes from './ConversationView.module.scss';
import {Conversation} from '../../model/conversation';
import {fetchConversationMessages} from '../../api/api';
import ConversationMessageTile from './ConversationMessageTile';
import {Message} from '../../model/message';

interface Props {
    conversation: Conversation|null
}

const ConversationView = ({conversation}: Props) => {
    const [messages, setMessages] = useState<Message[]>([]);
    useEffect(() => {
        if (conversation) {
            fetchConversationMessages(conversation.id).then(res => { setMessages(res.data) })
        }
    }, [conversation])
    const messageTiles = messages.map(m => <ConversationMessageTile message={m} key={m.id}/>)
    return (
        <div className={classes.Container}>{messageTiles}</div>
    );
}

export default ConversationView;
