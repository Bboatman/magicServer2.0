import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ICard } from 'app/shared/model/card.model';
import { getEntities as getCards } from 'app/entities/card/card.reducer';
import { IDeck } from 'app/shared/model/deck.model';
import { getEntities as getDecks } from 'app/entities/deck/deck.reducer';
import { getEntity, updateEntity, createEntity, reset } from './card-instance.reducer';
import { ICardInstance } from 'app/shared/model/card-instance.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const CardInstanceUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const cards = useAppSelector(state => state.card.entities);
  const decks = useAppSelector(state => state.deck.entities);
  const cardInstanceEntity = useAppSelector(state => state.cardInstance.entity);
  const loading = useAppSelector(state => state.cardInstance.loading);
  const updating = useAppSelector(state => state.cardInstance.updating);
  const updateSuccess = useAppSelector(state => state.cardInstance.updateSuccess);
  const handleClose = () => {
    props.history.push('/card-instance');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getCards({}));
    dispatch(getDecks({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...cardInstanceEntity,
      ...values,
      card: cards.find(it => it.id.toString() === values.card.toString()),
      deck: decks.find(it => it.id.toString() === values.deck.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...cardInstanceEntity,
          card: cardInstanceEntity?.card?.id,
          deck: cardInstanceEntity?.deck?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="magicMonolithicApp.cardInstance.home.createOrEditLabel" data-cy="CardInstanceCreateUpdateHeading">
            Create or edit a CardInstance
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="card-instance-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Count" id="card-instance-count" name="count" data-cy="count" type="text" />
              <ValidatedField label="Missing" id="card-instance-missing" name="missing" data-cy="missing" check type="checkbox" />
              <ValidatedField label="Parsed Name" id="card-instance-parsedName" name="parsedName" data-cy="parsedName" type="text" />
              <ValidatedField id="card-instance-card" name="card" data-cy="card" label="Card" type="select">
                <option value="" key="0" />
                {cards
                  ? cards.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="card-instance-deck" name="deck" data-cy="deck" label="Deck" type="select">
                <option value="" key="0" />
                {decks
                  ? decks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/card-instance" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default CardInstanceUpdate;
