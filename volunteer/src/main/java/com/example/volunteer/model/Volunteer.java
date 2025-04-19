package com.example.volunteer.model;

import com.example.volunteer.model.auth.User; // Импортируем auth.User
import jakarta.persistence.*; // Импортируем нужные аннотации JPA
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "volunteers") // Таблица для специфичных данных волонтеров
public class Volunteer {

    @Id
    @Column(name = "user_id") // Имя колонки для ID, совпадающее с ID в auth.User
    private String id; // Тип должен совпадать с User.id (String)

    // Связь OneToOne с основной таблицей пользователей
    @OneToOne(fetch = FetchType.LAZY) // Используем LAZY загрузку для оптимизации
    @MapsId // Указывает, что PK (id) также является FK к User
    @JoinColumn(name = "user_id") // Имя колонки внешнего ключа
    private User user; // Ссылка на объект User

    private String middleName;

    private Long organizationId; // ID организации, к которой принадлежит волонтер

    private String skills;
    private Double rating;

    public Volunteer(User user) {
        this.id = user.getId();
        this.user = user;
    }
}