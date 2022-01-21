import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CardInstance from './card-instance';
import CardInstanceDetail from './card-instance-detail';
import CardInstanceUpdate from './card-instance-update';
import CardInstanceDeleteDialog from './card-instance-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CardInstanceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CardInstanceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CardInstanceDetail} />
      <ErrorBoundaryRoute path={match.url} component={CardInstance} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CardInstanceDeleteDialog} />
  </>
);

export default Routes;
