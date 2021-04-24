import {User} from './user';

export interface Message {
    id: number,
    text: string,
    sender: User,
    conversationId: number,
    sent: Date
}
