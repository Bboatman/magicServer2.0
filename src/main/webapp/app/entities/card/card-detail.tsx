import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './card.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const CardDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const cardEntity = useAppSelector(state => state.card.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cardDetailsHeading">Card</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{cardEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{cardEntity.name}</dd>
          <dt>
            <span id="rarity">Rarity</span>
          </dt>
          <dd>{cardEntity.rarity}</dd>
          <dt>
            <span id="cardType">Card Type</span>
          </dt>
          <dd>{cardEntity.cardType}</dd>
          <dt>
            <span id="toughness">Toughness</span>
          </dt>
          <dd>{cardEntity.toughness}</dd>
          <dt>
            <span id="power">Power</span>
          </dt>
          <dd>{cardEntity.power}</dd>
          <dt>
            <span id="cmc">Cmc</span>
          </dt>
          <dd>{cardEntity.cmc}</dd>
          <dt>
            <span id="colorIdentity">Color Identity</span>
          </dt>
          <dd>{cardEntity.colorIdentity}</dd>
          <dt>
            <span id="x">X</span>
          </dt>
          <dd>{cardEntity.x}</dd>
          <dt>
            <span id="y">Y</span>
          </dt>
          <dd>{cardEntity.y}</dd>
        </dl>
        <Button tag={Link} to="/card" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/card/${cardEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default CardDetail;
