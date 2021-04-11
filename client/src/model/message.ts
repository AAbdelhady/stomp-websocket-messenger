import {User} from './user';

export interface Message {
    id: number,
    text: string,
    sender: User,
    sent: Date
}
