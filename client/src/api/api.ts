import axios from '../axios';
import {User} from '../model/user';
import {Conversation} from '../model/conversation';
import {Message} from '../model/message';

const USER_ROOT = 'user';
const CONVERSATION_ROOT = 'conversation';

export const fetchAuthorizedUser = () => axios.get<User>(`${USER_ROOT}/me`)

export const fetchAuthorizedUserConversations = () => axios.get<Conversation[]>(CONVERSATION_ROOT)

export const fetchConversationMessages = (conversationId: number) => axios.get<Message[]>(`${CONVERSATION_ROOT}/${conversationId}/messages`)

