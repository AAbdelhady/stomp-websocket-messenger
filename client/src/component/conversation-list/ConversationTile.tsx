import React, {useContext} from 'react';
import classes from './ConversationTile.module.scss';
import {Conversation} from '../../model/conversation';
import {User} from '../../model/user';
import AuthorizedUserContext from '../../contexts/authorizedUserContext';

interface Props {
    conversation: Conversation,
    conversationTileClicked: () => void
}

const ConversationTile = ({conversation, conversationTileClicked}: Props) => {
    const authorizedUser = useContext<User|null>(AuthorizedUserContext)
    const title = conversation.participants.filter(p => p.id !== authorizedUser?.id).map(p => `${p.firstName} ${p.lastName}`).join(', ')
    const avatar = conversation.lastMessage?.sender.profilePictureUrl
    return (
        <div className={classes.Container} onClick={conversationTileClicked}>
            <div className={classes.LeftSideContainer}>
                <div className={classes.Avatar} style={{backgroundImage: `url('${avatar}'`}}/>
            </div>
            <div className={classes.RightSideContainer}>
                <div className={classes.Title}><p>{title}</p></div>
                <div className={classes.LastMessageText}><p>{conversation.lastMessage?.text}</p></div>
            </div>
        </div>
    );
}

export default ConversationTile;