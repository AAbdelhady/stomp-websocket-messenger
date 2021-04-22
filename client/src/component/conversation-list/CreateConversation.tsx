import React, {useState} from 'react';
import classes from './CreateConversation.module.scss';
import {findUserByEmail, createConversationWithParticipants} from '../../api/api';
import {User} from '../../model/user';

const CreateConversation = () => {
    const [email, setEmail] = useState<string>('')
    const [users, setUsers] = useState<User[]>([])

    const onInputChange = (e: any) => { setEmail(e.target.value) }

    const onKeyPress = (e: any) => {
        if (e.key === 'Enter') { findUserByName() }
    }

    const findUserByName = () => {
        if (!email) { return }
        findUserByEmail(email).then(res => { onUserFound(res.data) }).catch(err => { console.warn(err) })
        setEmail('')
    }

    const onUserFound = (user: User) => { setUsers(current => [...current, user]) }

    const createConversation = () => {
        if (users.length > 0) {
            createConversationWithParticipants(users.map(u => u.id)).then(res => { console.info(res.data) })
        }
    }

    const memberAvatars = users.map(u => <div className={classes.Avatar} style={{backgroundImage: `url('${u.profilePictureUrl}'`}}/>)

    return (
        <div>
            <div className={classes.MemberAvatarsContainer}>{memberAvatars}</div>
            <div>
                <input type="text" value={email} onChange={onInputChange} onKeyPress={onKeyPress}/>
            </div>
            <div>
                <button onClick={createConversation}>Create Conversation</button>
            </div>
        </div>
    );
}

export default CreateConversation;
