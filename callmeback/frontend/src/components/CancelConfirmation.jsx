/**
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React from 'react';
import { Container } from 'semantic-ui-react';
import { Link } from 'react-router-dom';

function CancelConfirmation() {
  return (
    <Container
      text
      className='paper'
      data-testid='cancelconfirmation-container'
    >
      <div>
        You have canceled your call with us. We're here if you want to{' '}
        <Link to='/home'>Request a new call.</Link>
      </div>
    </Container>
  );
}

export default CancelConfirmation;
