import { ICard } from 'app/shared/model/card.model';
import { IDeck } from 'app/shared/model/deck.model';

export interface ICardInstance {
  id?: number;
  count?: number | null;
  missing?: boolean | null;
  parsedName?: string | null;
  card?: ICard | null;
  deck?: IDeck | null;
}

export const defaultValue: Readonly<ICardInstance> = {
  missing: false,
};
