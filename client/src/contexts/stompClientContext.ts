import React from 'react';
import {Client} from '@stomp/stompjs';

const StompClientContext = React.createContext<Client|null>(null)

export default StompClientContext;
