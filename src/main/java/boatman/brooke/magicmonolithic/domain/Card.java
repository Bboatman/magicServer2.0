package boatman.brooke.magicmonolithic.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Card.
 */
@Entity
@Table(name = "card")
public class Card implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "rarity")
    private Integer rarity;

    @Column(name = "card_type")
    private Long cardType;

    @Column(name = "toughness")
    private String toughness;

    @Column(name = "power")
    private Long power;

    @Column(name = "cmc")
    private Integer cmc;

    @Column(name = "color_identity")
    private Integer colorIdentity;

    @Column(name = "x")
    private Float x;

    @Column(name = "y")
    private Float y;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Card id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Card name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRarity() {
        return this.rarity;
    }

    public Card rarity(Integer rarity) {
        this.setRarity(rarity);
        return this;
    }

    public void setRarity(Integer rarity) {
        this.rarity = rarity;
    }

    public Long getCardType() {
        return this.cardType;
    }

    public Card cardType(Long cardType) {
        this.setCardType(cardType);
        return this;
    }

    public void setCardType(Long cardType) {
        this.cardType = cardType;
    }

    public String getToughness() {
        return this.toughness;
    }

    public Card toughness(String toughness) {
        this.setToughness(toughness);
        return this;
    }

    public void setToughness(String toughness) {
        this.toughness = toughness;
    }

    public Long getPower() {
        return this.power;
    }

    public Card power(Long power) {
        this.setPower(power);
        return this;
    }

    public void setPower(Long power) {
        this.power = power;
    }

    public Integer getCmc() {
        return this.cmc;
    }

    public Card cmc(Integer cmc) {
        this.setCmc(cmc);
        return this;
    }

    public void setCmc(Integer cmc) {
        this.cmc = cmc;
    }

    public Integer getColorIdentity() {
        return this.colorIdentity;
    }

    public Card colorIdentity(Integer colorIdentity) {
        this.setColorIdentity(colorIdentity);
        return this;
    }

    public void setColorIdentity(Integer colorIdentity) {
        this.colorIdentity = colorIdentity;
    }

    public Float getX() {
        return this.x;
    }

    public Card x(Float x) {
        this.setX(x);
        return this;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return this.y;
    }

    public Card y(Float y) {
        this.setY(y);
        return this;
    }

    public void setY(Float y) {
        this.y = y;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Card)) {
            return false;
        }
        return id != null && id.equals(((Card) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Card{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", rarity=" + getRarity() +
            ", cardType=" + getCardType() +
            ", toughness='" + getToughness() + "'" +
            ", power=" + getPower() +
            ", cmc=" + getCmc() +
            ", colorIdentity=" + getColorIdentity() +
            ", x=" + getX() +
            ", y=" + getY() +
            "}";
    }
}
