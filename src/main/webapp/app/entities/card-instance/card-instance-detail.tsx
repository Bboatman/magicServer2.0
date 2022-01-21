import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './card-instance.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const CardInstanceDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const cardInstanceEntity = useAppSelector(state => state.cardInstance.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cardInstanceDetailsHeading">CardInstance</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{cardInstanceEntity.id}</dd>
          <dt>
            <span id="count">Count</span>
          </dt>
          <dd>{cardInstanceEntity.count}</dd>
          <dt>
            <span id="missing">Missing</span>
          </dt>
          <dd>{cardInstanceEntity.missing ? 'true' : 'false'}</dd>
          <dt>
            <span id="parsedName">Parsed Name</span>
          </dt>
          <dd>{cardInstanceEntity.parsedName}</dd>
          <dt>Card</dt>
          <dd>{cardInstanceEntity.card ? cardInstanceEntity.card.id : ''}</dd>
          <dt>Deck</dt>
          <dd>{cardInstanceEntity.deck ? cardInstanceEntity.deck.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/card-instance" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/card-instance/${cardInstanceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default CardInstanceDetail;
