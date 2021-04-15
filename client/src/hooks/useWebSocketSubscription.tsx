import {useContext, useEffect, useState} from 'react';
import {Client, IMessage} from '@stomp/stompjs';
import StompClientContext from '../contexts/stompClientContext';

function useWebSocketSubscription(destination: string, onMessage: (m: IMessage) => void) {
    const stompClient = useContext<Client|null>(StompClientContext)
    const [subscribed, setSubscribed] = useState<boolean>()

    useEffect(() => {
        if (stompClient && !subscribed) {
            stompClient.subscribe(destination, onMessage)
            setSubscribed(true)
        }
        return () => stompClient?.unsubscribe(destination)
    }, [stompClient, destination, onMessage, subscribed])
}

export default useWebSocketSubscription;
