import React, {useState} from 'react';
import classes from './Login.module.scss';
import {loginWithDummyUser} from '../../api/api';

const Login = () => {
    const [dummyUserId, setDummyUserId] = useState('')

    const onInputChange = (e: any) => { setDummyUserId(e.target.value) }

    const dummyLogin = () => {
        if (dummyUserId && Number(dummyUserId)) {
            loginWithDummyUser(Number(dummyUserId))
                .then(() => window?.location.reload())
        }
    }

    return (
        <div className={classes.Container}>
            <div className={classes.Top}>
                <div className={classes.ButtonContainer}>
                    <a href={`${process.env.NEXT_PUBLIC_API_BASE_URL}/oauth2/authorization/google`}>Google Login</a>
                </div>
            </div>
            <div className={classes.Bottom}>
                <div>
                    <h3>Dummy Login</h3>
                    <input placeholder="Dummy user ID" type="number" value={dummyUserId} onChange={onInputChange}/>
                    <button onClick={dummyLogin} disabled={!dummyUserId}>Login</button>
                    <div className={classes.ExploreDummy}>
                        <a href={`${process.env.NEXT_PUBLIC_API_BASE_URL}/dummy`} target="_blank">explore dummy users ↗</a>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Login;
