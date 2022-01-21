import { ICardInstance } from 'app/shared/model/card-instance.model';

export interface IDeck {
  id?: number;
  name?: string | null;
  url?: string;
  cardInstances?: ICardInstance[] | null;
}

export const defaultValue: Readonly<IDeck> = {};
