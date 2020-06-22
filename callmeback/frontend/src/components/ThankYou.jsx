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
import {Container} from 'semantic-ui-react';
import {Link} from 'react-router-dom';

/**
 * @return {string} Container thanking the user for providing feedback
 */
function ThankYou() {
  return (
    <Container
      text
      style={{paddingTop: '2em'}}
      data-testid='thankyou-container'
    >
      <div>Thank you!</div>
      <br />
      <div>
        <Link to='/home'>Go to the New York State call homepage</Link>
      </div>
    </Container>
  );
}

export default ThankYou;
