export interface ICard {
  id?: number;
  name?: string;
  rarity?: number | null;
  cardType?: number | null;
  toughness?: string | null;
  power?: number | null;
  cmc?: number | null;
  colorIdentity?: number | null;
  x?: number | null;
  y?: number | null;
}

export const defaultValue: Readonly<ICard> = {};
