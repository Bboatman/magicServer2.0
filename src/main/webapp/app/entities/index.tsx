import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Card from './card';
import CardInstance from './card-instance';
import Deck from './deck';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}card`} component={Card} />
      <ErrorBoundaryRoute path={`${match.url}card-instance`} component={CardInstance} />
      <ErrorBoundaryRoute path={`${match.url}deck`} component={Deck} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
