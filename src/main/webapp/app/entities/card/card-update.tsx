import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './card.reducer';
import { ICard } from 'app/shared/model/card.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const CardUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const cardEntity = useAppSelector(state => state.card.entity);
  const loading = useAppSelector(state => state.card.loading);
  const updating = useAppSelector(state => state.card.updating);
  const updateSuccess = useAppSelector(state => state.card.updateSuccess);
  const handleClose = () => {
    props.history.push('/card');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...cardEntity,
      ...values,
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
          ...cardEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="magicserverApp.card.home.createOrEditLabel" data-cy="CardCreateUpdateHeading">
            Create or edit a Card
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="card-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Name"
                id="card-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Rarity" id="card-rarity" name="rarity" data-cy="rarity" type="text" />
              <ValidatedField label="Card Type" id="card-cardType" name="cardType" data-cy="cardType" type="text" />
              <ValidatedField label="Toughness" id="card-toughness" name="toughness" data-cy="toughness" type="text" />
              <ValidatedField label="Power" id="card-power" name="power" data-cy="power" type="text" />
              <ValidatedField label="Cmc" id="card-cmc" name="cmc" data-cy="cmc" type="text" />
              <ValidatedField label="Color Identity" id="card-colorIdentity" name="colorIdentity" data-cy="colorIdentity" type="text" />
              <ValidatedField label="X" id="card-x" name="x" data-cy="x" type="text" />
              <ValidatedField label="Y" id="card-y" name="y" data-cy="y" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/card" replace color="info">
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

export default CardUpdate;
