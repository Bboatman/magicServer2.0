import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Deck from './deck';
import Card from './card';
import CardInstance from './card-instance';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}deck`} component={Deck} />
      <ErrorBoundaryRoute path={`${match.url}card`} component={Card} />
      <ErrorBoundaryRoute path={`${match.url}card-instance`} component={CardInstance} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
