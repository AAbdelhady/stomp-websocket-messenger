import React, {useState} from 'react';
import classes from './SendMessageInput.module.scss';
import {Conversation} from '../../model/conversation';
import {sendMessageInConversation} from '../../api/api';
import {Message} from '../../model/message';

interface Props {
    conversation: Conversation,
    onMessageSent: (m: Message) => void
}

const SendMessageInput = ({conversation, onMessageSent}: Props) => {
    const [text, setText] = useState('')

    const onInputChange = (e: any) => { setText(e.target.value) }

    const onKeyPress = (e: any) => {
        if (e.key === 'Enter') { sendMessage() }
    }

    const sendMessage = () => {
        if (!text) { return }
        sendMessageInConversation(conversation.id, text).then(res => { onMessageSent(res.data) })
        setText('')
    }

    return (
        <div className={classes.Container}>
            <input type="text" value={text} onChange={onInputChange} onKeyPress={onKeyPress}/>
            <button onClick={sendMessage}>Send</button>
        </div>
    );
}

export default SendMessageInput;
