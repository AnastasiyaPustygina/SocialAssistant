package com.example.mainproject.domain;

import java.util.Objects;

public class Message {
    private int id;
    private String whose;
    private int chat_id;
    private String values;
    private String time;

    public Message(String whose, int chat_id, String values, String time) {
        this.whose = whose;
        this.chat_id = chat_id;
        this.values = values;
        this.time = time;
    }

    public Message(int id, String whose, int chat_id, String values, String time) {
        this.id = id;
        this.whose = whose;
        this.chat_id = chat_id;
        this.values = values;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getWhose() {
        return whose;
    }

    public int getChat_id() {
        return chat_id;
    }

    public String getValues() {
        return values;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", whose='" + whose + '\'' +
                ", chat_id=" + chat_id +
                ", values='" + values + '\'' +
                ", time='" + time + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return chat_id == message.chat_id && Objects.equals(whose, message.whose) && Objects.equals(values, message.values) && Objects.equals(time, message.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(whose, chat_id, values, time);
    }
}