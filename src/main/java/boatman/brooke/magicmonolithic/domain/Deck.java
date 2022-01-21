package boatman.brooke.magicmonolithic.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Deck.
 */
@Entity
@Table(name = "deck")
public class Deck implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "url", nullable = false, unique = true)
    private String url;

    @OneToMany(mappedBy = "deck")
    @JsonIgnoreProperties(value = { "card", "deck" }, allowSetters = true)
    private Set<CardInstance> cardInstances = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Deck id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Deck name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return this.url;
    }

    public Deck url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<CardInstance> getCardInstances() {
        return this.cardInstances;
    }

    public void setCardInstances(Set<CardInstance> cardInstances) {
        if (this.cardInstances != null) {
            this.cardInstances.forEach(i -> i.setDeck(null));
        }
        if (cardInstances != null) {
            cardInstances.forEach(i -> i.setDeck(this));
        }
        this.cardInstances = cardInstances;
    }

    public Deck cardInstances(Set<CardInstance> cardInstances) {
        this.setCardInstances(cardInstances);
        return this;
    }

    public Deck addCardInstance(CardInstance cardInstance) {
        this.cardInstances.add(cardInstance);
        cardInstance.setDeck(this);
        return this;
    }

    public Deck removeCardInstance(CardInstance cardInstance) {
        this.cardInstances.remove(cardInstance);
        cardInstance.setDeck(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Deck)) {
            return false;
        }
        return id != null && id.equals(((Deck) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Deck{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
