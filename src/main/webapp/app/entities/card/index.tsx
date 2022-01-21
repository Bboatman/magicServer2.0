import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Card from './card';
import CardDetail from './card-detail';
import CardUpdate from './card-update';
import CardDeleteDialog from './card-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CardUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CardUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CardDetail} />
      <ErrorBoundaryRoute path={match.url} component={Card} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CardDeleteDialog} />
  </>
);

export default Routes;
