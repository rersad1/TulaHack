package com.bubla.service;

import com.bubla.exception.ResourceNotFoundException;
import com.bubla.model.ChatMessage;
import com.bubla.model.ChatRoom;
import com.bubla.model.MessageStatus;
import com.bubla.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для упавления сообщениями
 */

@Service
public class ChatMessageService {
    @Autowired private ChatMessageRepository repository;
    @Autowired private ChatRoomService chatRoomService;

    /**
     * Изменяет статус сообщения на RECEIVED и сохраняет его
     * @param chatMessage
     * @return Созраненное сообщение
     */
    public ChatMessage save(ChatMessage chatMessage){
        chatMessage.setStatus(MessageStatus.RECEIVED);
        repository.save(chatMessage);
        return chatMessage;
    }

    /**
     * Возвращает количество новых сообщений
     * @param senderId
     * @param recipientId
     * @return количество новых сообщений
     */
    public long countNewMessages(String senderId, String recipientId){
        return repository.countBySenderIdAndRecipientIdAndStatus(senderId, recipientId, MessageStatus.RECEIVED);
    }

    /**
     * Возвращаетс список сообщений в чате
     * @param senderId
     * @param recipientId
     * @return список сообщений
     */
    public List<ChatMessage> findChatMessage(String senderId, String recipientId){
        var chatId = chatRoomService.getChatId(senderId, recipientId, true);
        var messages = chatId.map(cId -> repository.findAllByChatId(cId)).orElse(new ArrayList<>());

        if(!messages.isEmpty()){
            updateStatuses(messages, MessageStatus.DELIVERED);
        }

        return messages;
    }

    /**
     * Ищет сообщение по ID
     * @param id
     * @return найденной сообщение
     */
    public ChatMessage findById(String id){
        return repository
                .findById(id)
                .map(chatMessage -> {
                    chatMessage.setStatus(MessageStatus.DELIVERED);
                    return repository.save(chatMessage);
                })
                .orElseThrow(() -> new ResourceNotFoundException("can't find message(" + id + ")"));
    }

    /**
     * Меняет статусы у сообщение на DELIVERED
     * @param messages
     * @param status
     */
    public void updateStatuses(List<ChatMessage> messages, MessageStatus status){
            messages
                    .stream()
                    .map(recieved -> {
                        recieved.setStatus(MessageStatus.DELIVERED);
                        repository.save(recieved);
                        return recieved;
                    });
    }
}
