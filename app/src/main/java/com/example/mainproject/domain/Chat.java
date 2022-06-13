package com.example.mainproject.domain;

import java.util.Objects;

public class Chat {

    private int id;
    private Person person;
    private Organization organization;

    public Chat(Person person, Organization organization) {
        this.person = person;
        this.organization = organization;
    }

    public Chat(int id, Person person, Organization organization) {
        this.id = id;
        this.person = person;
        this.organization = organization;
    }

    public int getId() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public Organization getOrganization() {
        return organization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return id == chat.id && person.equals(chat.person) && organization.equals(chat.organization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, person, organization);
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", person=" + person +
                ", organization=" + organization +
                '}';
    }
}
