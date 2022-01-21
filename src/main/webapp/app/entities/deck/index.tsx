import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Deck from './deck';
import DeckDetail from './deck-detail';
import DeckUpdate from './deck-update';
import DeckDeleteDialog from './deck-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DeckUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DeckUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DeckDetail} />
      <ErrorBoundaryRoute path={match.url} component={Deck} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={DeckDeleteDialog} />
  </>
);

export default Routes;
