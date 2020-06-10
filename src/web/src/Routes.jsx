import React from 'react';
import { Route, Switch } from 'react-router-dom';
import Item from './components/Item.jsx';

function Routes() {
  return (
    <div>
      <Switch>
        <Route path='/items/:id' component={Item} />
        <Route exact path='/' component={Item} />
      </Switch>
    </div>
  );
}

export default Routes;
