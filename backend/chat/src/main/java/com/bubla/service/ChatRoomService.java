package com.bubla.service;

import com.bubla.model.ChatRoom;
import com.bubla.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Сервис для управления чат-коматой
 */
@Service
public class ChatRoomService {

    @Autowired private ChatRoomRepository chatRoomRepository;

    /**
     * Ищет ID чата по отправителю и получателю
     * @param senderId
     * @param recipientId
     * @param createIfNotExisted
     * @return ID чата
     */
    public Optional<String> getChatId(String senderId, String recipientId, boolean createIfNotExisted){
        return chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or(() ->{
                    if(!createIfNotExisted){
                        return Optional.empty();
                    }
                    var chatId = String.format("%s_%s", senderId, recipientId);
                    ChatRoom senderRecipient = ChatRoom
                            .builder()
                            .chatId(chatId)
                            .senderId(senderId)
                            .recipientId(recipientId)
                            .build();
                    ChatRoom recipientSender =  ChatRoom
                            .builder()
                            .chatId(chatId)
                            .senderId(recipientId)
                            .recipientId(senderId)
                            .build();
                    chatRoomRepository.save(senderRecipient);
                    chatRoomRepository.save(recipientSender);

                    return Optional.of(chatId);
                        });
    }
}
