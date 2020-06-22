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
import {useLocation} from 'react-router';

/**
 * @return {string} div containing the NYS header
 */
function Header() {
  const location = useLocation();

  return (
    <div>
      {location.pathname === '/' || location.pathname === '/home' ? (
        <header>
          <img src='/nygov-logo.png' alt='New York State logo' />
        </header>
      ) : (
        <div className='corner-logo'>
          <img
            src='/nygov-logo.png'
            alt='New York State logo'
            style={{width: '5em'}}
          />
        </div>
      )}
    </div>
  );
}

export default Header;
