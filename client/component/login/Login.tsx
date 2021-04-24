import React from 'react';
import classes from './Login.module.scss';

const Login = () => {
    return (
        <div className={classes.Container}>
            <div className={classes.ButtonContainer}>
                <a href={`${process.env.NEXT_PUBLIC_API_BASE_URL}/oauth2/authorization/google`}>Google</a>
            </div>
            <div className={classes.ButtonContainer}>
                <a href={`${process.env.NEXT_PUBLIC_API_BASE_URL}/oauth2/authorization/facebook`}>Facebook</a>
            </div>
        </div>
    );
}

export default Login;
