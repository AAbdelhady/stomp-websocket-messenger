import React, {useEffect, useState} from 'react';
import classes from './ConversationView.module.scss';
import {Conversation} from '../../model/conversation';
import {fetchConversationMessages} from '../../api/api';
import ConversationMessageTile from './ConversationMessageTile';
import {Message} from '../../model/message';
import SendMessageInput from './SendMessageInput';
import useWebSocketSubscription from '../../hooks/useWebSocketSubscription';
import {IMessage} from '@stomp/stompjs';

interface Props {
    conversation: Conversation
}

const ConversationView = ({conversation}: Props) => {
    const [messages, setMessages] = useState<Message[]>([]);

    useEffect(() => {
            fetchConversationMessages(conversation.id).then(res => { setMessages(res.data) })
    }, [conversation])

    const messageTiles = messages.map(m => <ConversationMessageTile message={m} key={m.id}/>)

    const pushNewMessage = (m: Message) => setMessages(existing =>[...existing, m])


    useWebSocketSubscription("/user/queue/message/message", (m: IMessage) => {
        const newMessage: Message = JSON.parse(m.body).payload
        if (conversation?.id === newMessage.conversationId) {
            pushNewMessage(newMessage)
        }
    })

    return (
        <div className={classes.Container}>
            <div className={classes.MessageListContainer}>
                {messageTiles}
            </div>
            <div className={classes.SendMessageInputContainer}>
                <SendMessageInput conversation={conversation} onMessageSent={pushNewMessage}/>
            </div>
        </div>
    );
}

export default ConversationView;
