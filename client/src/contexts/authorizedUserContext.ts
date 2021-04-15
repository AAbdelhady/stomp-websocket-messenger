import React from 'react';
import {User} from '../model/user';

const AuthorizedUserContext = React.createContext<User|null>(null)

export default AuthorizedUserContext;
