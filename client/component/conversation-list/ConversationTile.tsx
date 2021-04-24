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
    const title = conversation.participants.map(p => `${p.firstName} ${p.lastName}`).join(', ')
    const avatars = conversation.participants.map(p => <span className={classes.Avatar} key={p.id}><img src={p.profilePictureUrl} alt="profile-pic"/></span>)
    return (
        <div className={classes.Container} onClick={conversationTileClicked}>
            <div className={classes.LeftSideContainer}>
                <div className={classes.Avatars}>{avatars}</div>
            </div>
            <div className={classes.RightSideContainer}>
                <div className={classes.Title}><p>{title}</p></div>
                <div className={classes.LastMessageText}><p>{conversation.lastMessage?.text}</p></div>
            </div>
        </div>
    );
}

export default ConversationTile;
