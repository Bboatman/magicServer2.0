import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './card.reducer';
import { ICard } from 'app/shared/model/card.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Card = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const cardList = useAppSelector(state => state.card.entities);
  const loading = useAppSelector(state => state.card.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="card-heading" data-cy="CardHeading">
        Cards
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Card
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {cardList && cardList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Rarity</th>
                <th>Card Type</th>
                <th>Toughness</th>
                <th>Power</th>
                <th>Cmc</th>
                <th>Color Identity</th>
                <th>X</th>
                <th>Y</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {cardList.map((card, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${card.id}`} color="link" size="sm">
                      {card.id}
                    </Button>
                  </td>
                  <td>{card.name}</td>
                  <td>{card.rarity}</td>
                  <td>{card.cardType}</td>
                  <td>{card.toughness}</td>
                  <td>{card.power}</td>
                  <td>{card.cmc}</td>
                  <td>{card.colorIdentity}</td>
                  <td>{card.x}</td>
                  <td>{card.y}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${card.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${card.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${card.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Cards found</div>
        )}
      </div>
    </div>
  );
};

export default Card;
