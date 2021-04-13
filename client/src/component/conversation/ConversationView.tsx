import React, {useEffect, useState} from 'react';
import classes from './ConversationView.module.scss';
import {Conversation} from '../../model/conversation';
import {fetchConversationMessages} from '../../api/api';
import ConversationMessageTile from './ConversationMessageTile';
import {Message} from '../../model/message';
import SendMessageInput from './SendMessageInput';

interface Props {
    conversation: Conversation
}

const ConversationView = ({conversation}: Props) => {
    const [messages, setMessages] = useState<Message[]>([]);
    useEffect(() => {
            fetchConversationMessages(conversation.id).then(res => { setMessages(res.data) })
    }, [conversation])
    const messageTiles = messages.map(m => <ConversationMessageTile message={m} key={m.id}/>)
    const onMessageSent = (m: Message) => setMessages([...messages, m])
    return (
        <div className={classes.Container}>
            <div className={classes.MessageListContainer}>
                {messageTiles}
            </div>
            <div className={classes.SendMessageInputContainer}>
                <SendMessageInput conversation={conversation} onMessageSent={onMessageSent}/>
            </div>
        </div>
    );
}

export default ConversationView;
