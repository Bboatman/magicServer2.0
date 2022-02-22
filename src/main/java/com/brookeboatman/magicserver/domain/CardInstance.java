package com.brookeboatman.magicserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A CardInstance.
 */
@Entity
@Table(name = "card_instance")
public class CardInstance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "count")
    private Integer count;

    @Column(name = "missing")
    private Boolean missing;

    @Column(name = "parsed_name")
    private String parsedName;

    @OneToOne
    @JoinColumn(unique = true)
    private Card card;

    @ManyToOne
    @JsonIgnoreProperties(value = { "cardInstances" }, allowSetters = true)
    private Deck deck;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CardInstance id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCount() {
        return this.count;
    }

    public CardInstance count(Integer count) {
        this.setCount(count);
        return this;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Boolean getMissing() {
        return this.missing;
    }

    public CardInstance missing(Boolean missing) {
        this.setMissing(missing);
        return this;
    }

    public void setMissing(Boolean missing) {
        this.missing = missing;
    }

    public String getParsedName() {
        return this.parsedName;
    }

    public CardInstance parsedName(String parsedName) {
        this.setParsedName(parsedName);
        return this;
    }

    public void setParsedName(String parsedName) {
        this.parsedName = parsedName;
    }

    public Card getCard() {
        return this.card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public CardInstance card(Card card) {
        this.setCard(card);
        return this;
    }

    public Deck getDeck() {
        return this.deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public CardInstance deck(Deck deck) {
        this.setDeck(deck);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardInstance)) {
            return false;
        }
        return id != null && id.equals(((CardInstance) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardInstance{" +
            "id=" + getId() +
            ", count=" + getCount() +
            ", missing='" + getMissing() + "'" +
            ", parsedName='" + getParsedName() + "'" +
            "}";
    }
}
