import React from 'react';
import classes from './ConversationMessageTile.module.scss';
import {Message} from '../../model/message';
import useAuthorizedUser from '../../hooks/useAuthorizedUser';

interface Props {
    message: Message
}

const ConversationMessageTile = ({message}: Props) => {
    const authorizedUser = useAuthorizedUser()
    const messageTileContent = message.sender.id === authorizedUser?.id ?
        myMessageContent(message) :
        someoneElseMessageContent(message)
    return (
        <div className={classes.Container}>
            {messageTileContent}
        </div>
    );
}

const myMessageContent = ({text}: Message) => (
    <div className={`${classes.Content} ${classes.Mine}`}>
        <div className={classes.MessageBody}>
            <p>{text}</p>
        </div>
    </div>
);

const someoneElseMessageContent = ({sender, text}: Message) => (
    <div className={classes.Content}>
        <div className={classes.Sender}>
            <p>{`${sender.firstName} ${sender.lastName}`}:</p>
        </div>
        <div className={classes.MessageBody}>
            <p>{text}</p>
        </div>
    </div>
);

export default ConversationMessageTile;
