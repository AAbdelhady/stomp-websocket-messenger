import {useContext, useEffect, useRef} from 'react';
import {Client, IMessage, StompSubscription} from '@stomp/stompjs';
import StompClientContext from '../contexts/stompClientContext';

function useWebSocketSubscription(destination: string, onMessage: (m: IMessage) => void) {
    const stompClient = useContext<Client|null>(StompClientContext)
    const subscription = useRef<StompSubscription>()

    useEffect(() => {
        if (stompClient && !subscription.current) {
            console.log('subscribing to ' + destination)
            subscription.current = stompClient.subscribe(destination, onMessage)
        }
        return () => {
            subscription.current?.unsubscribe()
            subscription.current = undefined
        }
    }, [stompClient, destination, onMessage])
}

export default useWebSocketSubscription;
