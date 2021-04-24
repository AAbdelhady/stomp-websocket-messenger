import {useEffect, useState} from 'react';
import {fetchAuthorizedUser} from '../api/api';
import {User} from '../model/user';

function useAuthorizedUser() {
    const [authorizedUser, setAuthorizedUser] = useState<User | null>(null);

    useEffect(() => { fetch(setAuthorizedUser) }, []);

    return authorizedUser;
}

const fetch = (setAuthorizedUser: any) => {
    fetchAuthorizedUser().then(res => { setAuthorizedUser(res.data) })
        .catch(err => { if (err?.response?.status !== 401) { throw err } })
};

export default useAuthorizedUser;
