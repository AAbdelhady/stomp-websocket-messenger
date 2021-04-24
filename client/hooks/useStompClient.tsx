import {useEffect, useState} from 'react';
import {User} from '../model/user';
import {Client, IFrame} from '@stomp/stompjs';

function useStompClient(user: User|null) {
    const [stompClient, setStompClient] = useState<Client | null>(null);

    useEffect(() => {
        if (!user) {
            return
        }
        const client = new Client({
            brokerURL: process.env.NEXT_PUBLIC_WEBSOCKET_BASE_URL,
            connectHeaders: {
                login: user.id.toString(),
                passcode: '123',
            }
        })

        client.onConnect = (receipt: IFrame) => {
            console.log('Connected', receipt)
            setStompClient(client)
        }

        client.onDisconnect = (receipt: IFrame) => {
            console.log('Disconnected', receipt)
            setStompClient(null)
        }

        client.onStompError = (receipt: IFrame) => {
            console.log('Broker reported error: ' + receipt.headers['message']);
            console.log('Additional details: ' + receipt.body)
            setStompClient(null)
        }

        client.activate()

        return () => { client.deactivate() }
    }, [user]);

    return stompClient;
}

export default useStompClient;
