import React from 'react';
import {AppProps} from 'next/app';
import '../styles.scss';

const MessengerApp = ({Component, pageProps}: AppProps) => {
    return <Component {...pageProps} />
}

export default MessengerApp
