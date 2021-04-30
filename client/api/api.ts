import axios from './axios';
import {User} from '../model/user';
import {Conversation} from '../model/conversation';
import {Message} from '../model/message';

const USER_ROOT = 'user';
const CONVERSATION_ROOT = 'conversation';
const DUMMY_ROOT = 'dummy';

export const fetchAuthorizedUser = () => axios.get<User>(`${USER_ROOT}/me`)

export const findUserByEmail = (email: string) => axios.get<User>(`${USER_ROOT}/search/${email}`)

export const fetchAuthorizedUserConversations = () => axios.get<Conversation[]>(CONVERSATION_ROOT)

export const fetchConversationMessages = (conversationId: number) => axios.get<Message[]>(`${CONVERSATION_ROOT}/${conversationId}/messages`)

export const createConversationWithParticipants = (participantsIds: number[]) => axios.post<Conversation>(CONVERSATION_ROOT, {participantsIds: participantsIds})

export const sendMessageInConversation = (conversationId: number, text: string) => axios.post<Message>(`${CONVERSATION_ROOT}/${conversationId}`, {text: text})

export const logout = () => axios.post('logout')

export const loginWithDummyUser = (userId: number) => axios.post(`${DUMMY_ROOT}/login/${userId}`)

